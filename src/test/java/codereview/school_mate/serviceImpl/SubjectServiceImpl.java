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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
@Testcontainers
class StudentServiceImplTest {

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
    }
    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    @Test
    void create_ShouldPersistStudentWithAllRequiredFields() {
        Parent parent = parentRepository.save(createValidParent());
        SchoolClass schoolClass = schoolClassRepository.save(createValidSchoolClass());

        StudentRequestDto request = new StudentRequestDto();
        request.setName("Alice");
        request.setSurname("Johnson");
        request.setPatronymic("Marie");
        request.setParentId(parent.getId());
        request.setSchoolClassId(schoolClass.getId());

        StudentResponseDto result = studentService.createStudent(request);

        assertNotNull(result.getId());
        assertEquals(1, studentRepository.count());

        Student saved = studentRepository.findById(result.getId()).orElseThrow();
        assertEquals("Alice", saved.getName());
        assertEquals(parent.getId(), saved.getParent().getId());
        assertEquals(schoolClass.getId(), saved.getSchoolClass().getId());
    }

    @Test
    void findById_ShouldReturnStudentWithAllFields() {
        Parent parent = parentRepository.save(createValidParent());
        SchoolClass schoolClass = schoolClassRepository.save(createValidSchoolClass());
        Student student = studentRepository.save(createValidStudent(parent, schoolClass));

        StudentResponseDto result = studentService.findByIdStudent(student.getId());

        assertEquals(student.getId(), result.getId());
        assertEquals("Alice", result.getName());
        assertEquals("Johnson", result.getSurname());
        assertEquals("Marie", result.getPatronymic());
    }

    @Test
    void update_ShouldModifyStudentData() {
        Parent parent = parentRepository.save(createValidParent());
        SchoolClass schoolClass = schoolClassRepository.save(createValidSchoolClass());
        Student student = studentRepository.save(createValidStudent(parent, schoolClass));

        StudentRequestDto updateRequest = new StudentRequestDto();
        updateRequest.setName("Updated");
        updateRequest.setSurname("Name");
        updateRequest.setPatronymic("Patronymic");
        updateRequest.setParentId(parent.getId());
        updateRequest.setSchoolClassId(schoolClass.getId());

        studentService.updateStudent(student.getId(), updateRequest);

        Student updated = studentRepository.findById(student.getId()).orElseThrow();
        assertEquals("Updated", updated.getName());
        assertEquals("Name", updated.getSurname());
        assertEquals("Patronymic", updated.getPatronymic());
    }

    @Test
    void delete_ShouldRemoveStudentFromDB() {
        Parent parent = parentRepository.save(createValidParent());
        SchoolClass schoolClass = schoolClassRepository.save(createValidSchoolClass());
        Student student = studentRepository.save(createValidStudent(parent, schoolClass));

        studentService.deleteStudent(student.getId());

        assertFalse(studentRepository.existsById(student.getId()));
    }
    private Parent createValidParent() {
        Parent parent = new Parent();
        parent.setName("John");
        parent.setSurname("Doe");
        parent.setPatronymic("Smith");
        parent.setContacts("john@example.com");
        return parent;
    }

    private SchoolClass createValidSchoolClass() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("1-A");
        return schoolClass;
    }

    private Student createValidStudent(Parent parent, SchoolClass schoolClass) {
        Student student = new Student();
        student.setName("Alice");
        student.setSurname("Johnson");
        student.setPatronymic("Marie");
        student.setParent(parent);
        student.setSchoolClass(schoolClass);
        return student;
    }

}
