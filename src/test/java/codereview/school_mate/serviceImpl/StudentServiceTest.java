
package codereview.school_mate.serviceImpl;

import codereview.school_mate.dto.request.registration.StudentRegistrationRequestDto;
import codereview.school_mate.dto.request.StudentRequestDto;
import codereview.school_mate.dto.responce.StudentResponseDto;
import codereview.school_mate.mapper.StudentMapper;
import codereview.school_mate.model.*;
import codereview.school_mate.repository.*;

import codereview.school_mate.service.serviceImpl.StudentServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
@Testcontainers
@Transactional
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class StudentServiceTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private SchoolClassRepository schoolClassRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentServiceImpl studentService;

    private AtomicLong counter = new AtomicLong(1);

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withReuse(true);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.hikari.max-lifetime", () -> "15000");
        registry.add("spring.datasource.hikari.connection-timeout", () -> "5000");
    }

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        parentRepository.deleteAll();
        schoolClassRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        counter.set(1);
    }

    @AfterAll
    static void tearDown() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        postgres.stop();
    }

    @Test
    void create_ShouldSuccessfullyCreateStudent() {
        SchoolClass schoolClass = createTestClass();

        StudentRegistrationRequestDto request = new StudentRegistrationRequestDto();
        request.setName("New");
        request.setSurname("Student");
        request.setPatronymic("TestPatronymic");
        request.setSchoolClassId(schoolClass.getId());
        User user =createUser();

        StudentResponseDto result = studentService.createStudent(request, user);

        assertNotNull(result.getId());
        assertEquals("New", result.getName());
        assertEquals("Student", result.getSurname());
        assertEquals("TestPatronymic", result.getPatronymic());
//        assertEquals(parent.getId(), result.getParent().getId());
//        assertEquals(schoolClass.getId(), result.getSchoolClass().getId());
    }

    @Test
    void findById_ShouldReturnExistingStudent() {
        Student student = createTestStudent();

        StudentResponseDto result = studentService.findByIdStudent(student.getId());

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        assertEquals(student.getName(), result.getName());
        assertEquals(student.getSurname(), result.getSurname());
    }

    @Test
    void findAll_ShouldReturnAllStudents() {
        createTestStudent();
        createTestStudent();

        List<StudentResponseDto> result = studentService.findAllStudent();

        assertEquals(2, result.size());
    }

    @Test
    void update_ShouldUpdateStudentData() {
        Student student = createTestStudent();
        Parent newParent = createTestParent();
        SchoolClass newClass = createTestClass();

        StudentRequestDto updateRequest = new StudentRequestDto();
        updateRequest.setName("Updated");
        updateRequest.setSurname("Student");

        updateRequest.setPatronymic("TestPatronymic");
        updateRequest.setParentId(newParent.getId());
        updateRequest.setSchoolClassId(newClass.getId());

        StudentResponseDto result = studentService.updateStudent(student.getId(), updateRequest);

        assertEquals("Updated", result.getName());
        assertEquals("Student", result.getSurname());
//        assertEquals(newParent.getId(), result.getParent().getId());
//        assertEquals(newClass.getId(), result.getSchoolClass().getId());
    }

    @Test
    void delete_ShouldRemoveStudent() {
        Student student = createTestStudent();

        studentService.deleteStudent(student.getId());

        assertFalse(studentRepository.existsById(student.getId()));
    }

    @Test
    void create_ShouldThrowExceptionWhenParentNotFound() {
        SchoolClass schoolClass = createTestClass();
        Long nonExistentParentId = 999L;

        StudentRegistrationRequestDto request = new StudentRegistrationRequestDto();
        request.setName("New");
        request.setSurname("Student");
        request.setPatronymic("TestPatronymic");

//        request.setParentId(nonExistentParentId);
        request.setSchoolClassId(schoolClass.getId());

        assertThrows(RuntimeException.class, () -> studentService.createStudent(request, new User()));
    }

    @Test
    void create_ShouldThrowExceptionWhenClassNotFound() {
        Long nonExistentClassId = 999L;

        StudentRegistrationRequestDto request = new StudentRegistrationRequestDto();
        request.setName("New");
        request.setSurname("Student");
        request.setPatronymic("TestPatronymic");

        request.setSchoolClassId(nonExistentClassId);
        User user = new User();
        user.setUsername("student");
        user.setPassword("student");

        assertThrows(RuntimeException.class, () -> studentService.createStudent(request, user));
    }

    @Test
    void findById_ShouldThrowExceptionWhenStudentNotFound() {
        Long nonExistentId = 999L;

        assertThrows(RuntimeException.class, () -> studentService.findByIdStudent(nonExistentId));
    }

    @Test
    void update_ShouldThrowExceptionWhenStudentNotFound() {
        Long nonExistentId = 999L;
        StudentRequestDto updateRequest = new StudentRequestDto();
        updateRequest.setName("Nonexistent");

        assertThrows(RuntimeException.class, () -> studentService.updateStudent(nonExistentId, updateRequest));
    }

    @Test
    void update_ShouldThrowExceptionWhenNewParentNotFound() {
        Student student = createTestStudent();
        Long nonExistentParentId = 999L;

        StudentRequestDto updateRequest = new StudentRequestDto();
        updateRequest.setParentId(nonExistentParentId);

        assertThrows(RuntimeException.class, () -> studentService.updateStudent(student.getId(), updateRequest));
    }

    @Test
    void update_ShouldThrowExceptionWhenNewClassNotFound() {
        Student student = createTestStudent();
        Long nonExistentClassId = 999L;

        StudentRequestDto updateRequest = new StudentRequestDto();
        updateRequest.setSchoolClassId(nonExistentClassId);

        assertThrows(RuntimeException.class, () -> studentService.updateStudent(student.getId(), updateRequest));
    }

    @Test
    void delete_ShouldNotThrowExceptionWhenStudentNotFound() {
        Long nonExistentId = 999L;

        assertDoesNotThrow(() -> studentService.deleteStudent(nonExistentId));
    }

    @Test
    void create_ShouldThrowExceptionWhenNameFieldsAreInvalid() {
        SchoolClass schoolClass = createTestClass();

        StudentRegistrationRequestDto request = new StudentRegistrationRequestDto();
        request.setName("");
        request.setSurname("  ");
        request.setPatronymic(null);
        request.setSchoolClassId(schoolClass.getId());
        User user = createUser();

        assertThrows(DataIntegrityViolationException.class, () -> studentService.createStudent(request, user));
    }

    @Test
    void update_ShouldHandlePartialUpdate() {
        Student student = createTestStudent();
        String originalSurname = student.getSurname();
        String originalPatronymic = student.getPatronymic();

        StudentRequestDto updateRequest = new StudentRequestDto();
        updateRequest.setName("UpdatedFirstname");

        StudentResponseDto result = studentService.updateStudent(student.getId(), updateRequest);

        assertEquals("UpdatedFirstname", result.getName());
        assertEquals(originalSurname, result.getSurname());
        assertEquals(originalPatronymic, result.getPatronymic());
//        assertEquals(originalParentId, result.getParent().getId());
//        assertEquals(originalClassId, result.getSchoolClass().getId());
    }

    private Parent createTestParent() {
        long id = counter.getAndIncrement();
        Parent parent = new Parent();
        parent.setName("Parent_" + id);
        parent.setSurname("Surname_" + id);
        parent.setPatronymic("Patronymic_" + id);
        parent.setContacts("parent_" + id + "@test.com");
        return parentRepository.save(parent);
    }

    private SchoolClass createTestClass() {
        long id = counter.getAndIncrement();
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Class_" + id);
        return schoolClassRepository.save(schoolClass);
    }

    private User createUser() {
        long id = counter.getAndIncrement();
        Role role = new Role();
        role.setName("student_" + counter.getAndIncrement());
        roleRepository.save(role);
        User user = new User();
        user.setUsername("name_" + counter.getAndIncrement());
        user.setPassword("pass_" + counter.getAndIncrement());
        user.setRole(role);
        return userRepository.save(user);
    }

    private Student createTestStudent() {
//        Parent parent = createTestParent();
        SchoolClass schoolClass = createTestClass();
        User user = createUser();

        Student student = new Student();
        student.setName("Student_" + counter.getAndIncrement());
        student.setSurname("Surname_" + counter.getAndIncrement());
        student.setPatronymic("Patronymic_" + counter.getAndIncrement());

//        student.setParent(parent);
        student.setSchoolClass(schoolClass);
        student.setUser(user);
        return studentRepository.save(student);
    }
}