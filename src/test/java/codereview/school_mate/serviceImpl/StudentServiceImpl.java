
package codereview.school_mate.serviceImpl;

import codereview.school_mate.dto.StudentRequestDto;
import codereview.school_mate.dto.StudentResponseDto;
import codereview.school_mate.mapper.StudentMapper;
import codereview.school_mate.model.Parent;
import codereview.school_mate.model.SchoolClass;
import codereview.school_mate.model.Student;
import codereview.school_mate.repository.ParentRepository;
import codereview.school_mate.repository.SchoolClassRepository;
import codereview.school_mate.repository.StudentRepository;

import codereview.school_mate.service.serviceImpl.StudentServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
class StudentServiceTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private SchoolClassRepository schoolClassRepository;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentServiceImpl studentService;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        parentRepository.deleteAll();
        schoolClassRepository.deleteAll();
        counter.set(1);
    }
    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    @Test
    void create_ShouldSuccessfullyCreateStudent() {
        Parent parent = createTestParent();
        SchoolClass schoolClass = createTestClass();

        StudentRequestDto request = new StudentRequestDto();
        request.setName("New");
        request.setSurname("Student");
        request.setPatronymic("TestMiddleName");
        request.setParentId(parent.getId());
        request.setSchoolClassId(schoolClass.getId());

        StudentResponseDto result = studentService.createStudent(request);

        assertNotNull(result.getId());
        assertEquals("New", result.getName());
        assertEquals("Student", result.getSurname());
        assertEquals("TestMiddleName", result.getPatronymic());
        assertEquals(parent.getId(), result.getParent().getId());
        assertEquals(schoolClass.getId(), result.getSchoolClass().getId());
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
        updateRequest.setPatronymic("TestMiddleName");
        updateRequest.setParentId(newParent.getId());
        updateRequest.setSchoolClassId(newClass.getId());

        StudentResponseDto result = studentService.updateStudent(student.getId(), updateRequest);

        assertEquals("Updated", result.getName());
        assertEquals("Student", result.getSurname());
        assertEquals(newParent.getId(), result.getParent().getId());
        assertEquals(newClass.getId(), result.getSchoolClass().getId());
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

        StudentRequestDto request = new StudentRequestDto();
        request.setName("New");
        request.setSurname("Student");
        request.setPatronymic("TestMiddleName");
        request.setParentId(nonExistentParentId);
        request.setSchoolClassId(schoolClass.getId());

        assertThrows(RuntimeException.class, () -> studentService.createStudent(request));
    }

    @Test
    void create_ShouldThrowExceptionWhenClassNotFound() {
        Parent parent = createTestParent();
        Long nonExistentClassId = 999L;

        StudentRequestDto request = new StudentRequestDto();
        request.setName("New");
        request.setSurname("Student");
        request.setPatronymic("TestMiddleName");
        request.setParentId(parent.getId());
        request.setSchoolClassId(nonExistentClassId);

        assertThrows(RuntimeException.class, () -> studentService.createStudent(request));
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
        Parent parent = createTestParent();
        SchoolClass schoolClass = createTestClass();

        StudentRequestDto request = new StudentRequestDto();
        request.setName("");
        request.setSurname("  ");
        request.setPatronymic(null);
        request.setParentId(parent.getId());
        request.setSchoolClassId(schoolClass.getId());

        assertThrows(DataIntegrityViolationException.class, () -> studentService.createStudent(request));
    }

    @Test
    void update_ShouldHandlePartialUpdate() {
        Student student = createTestStudent();
        String originalSurname = student.getSurname();
        String originalPatronymic = student.getPatronymic();
        Long originalParentId = student.getParent().getId();
        Long originalClassId = student.getSchoolClass().getId();

        StudentRequestDto updateRequest = new StudentRequestDto();
        updateRequest.setName("UpdatedFirstname");

        StudentResponseDto result = studentService.updateStudent(student.getId(), updateRequest);

        assertEquals("UpdatedFirstname", result.getName());
        assertEquals(originalSurname, result.getSurname());
        assertEquals(originalPatronymic, result.getPatronymic());
        assertEquals(originalParentId, result.getParent().getId());
        assertEquals(originalClassId, result.getSchoolClass().getId());
    }

    private AtomicLong counter = new AtomicLong(1);

    private Parent createTestParent() {
        long id = counter.getAndIncrement();
        Parent parent = new Parent();
        parent.setName("Parent_" + id);
        parent.setSurname("LastName_" + id);
        parent.setPatronymic("MiddleName_" + id);
        parent.setContacts("parent_" + id + "@test.com");
        return parentRepository.save(parent);
    }

    private SchoolClass createTestClass() {
        long id = counter.getAndIncrement();
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Class_" + id);
        return schoolClassRepository.save(schoolClass);
    }

    private Student createTestStudent() {
        Parent parent = createTestParent();
        SchoolClass schoolClass = createTestClass();

        Student student = new Student();
        student.setName("Student_" + counter.getAndIncrement());
        student.setSurname("LastName_" + counter.getAndIncrement());
        student.setPatronymic("MiddleName_" + counter.getAndIncrement());
        student.setParent(parent);
        student.setSchoolClass(schoolClass);
        return studentRepository.save(student);
    }
}