package codereview.school_mate.serviceImpl;

import codereview.school_mate.dto.TeacherRequestDto;
import codereview.school_mate.dto.TeacherResponseDto;
import codereview.school_mate.mapper.TeacherMapper;
import codereview.school_mate.model.Subject;
import codereview.school_mate.model.Teacher;
import codereview.school_mate.repository.SchoolClassRepository;
import codereview.school_mate.repository.SubjectRepository;
import codereview.school_mate.repository.TeacherRepository;
import codereview.school_mate.service.serviceImpl.TeacherServiceImpl;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
@Testcontainers
class TeacherServiceTest {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SchoolClassRepository schoolClassRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherServiceImpl teacherService;

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
        teacherRepository.deleteAll();
        schoolClassRepository.deleteAll();
        subjectRepository.deleteAll();
        counter.set(1);
    }
    @AfterAll
     static void tearDown() {
        postgres.stop();
    }

    @Test
    void create_ShouldSuccessfullyCreateTeacher() {
        TeacherRequestDto request = new TeacherRequestDto();
        request.setName("John");
        request.setLastName("Doe");
        request.setPatronymic("Smith");

        TeacherResponseDto result = teacherService.createTeacher(request);

        assertNotNull(result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getLastName());
        assertEquals("Smith", result.getPatronymic());
    }

    @Test
    void findById_ShouldReturnExistingTeacher() {
        Teacher teacher = createTestTeacher();

        TeacherResponseDto result = teacherService.findByIdTeacher(teacher.getId());

        assertNotNull(result);
        assertEquals(teacher.getId(), result.getId());
        assertEquals(teacher.getName(), result.getName());
        assertEquals(teacher.getSurname(), result.getLastName());
    }

    @Test
    void findAll_ShouldReturnAllTeachers() {
        createTestTeacher();
        createTestTeacher();

        List<TeacherResponseDto> result = teacherService.findAllTeacher();

        assertEquals(2, result.size());
    }

    @Test
    void update_ShouldUpdateTeacherData() {
        Teacher teacher = createTestTeacher();

        TeacherRequestDto updateRequest = new TeacherRequestDto();
        updateRequest.setName("Updated");
        updateRequest.setLastName("Name");
        updateRequest.setPatronymic("Patronymic");

        TeacherResponseDto result = teacherService.updateTeacher(teacher.getId(), updateRequest);

        assertEquals("Updated", result.getName());
        assertEquals("Name", result.getLastName());
        assertEquals("Patronymic", result.getPatronymic());
    }

    @Test
    void delete_ShouldRemoveTeacher() {
        Teacher teacher = createTestTeacher();

        teacherService.deleteTeacher(teacher.getId());

        assertFalse(teacherRepository.existsById(teacher.getId()));
    }

    @Test
    void findById_ShouldThrowExceptionWhenTeacherNotFound() {
        Long nonExistentId = 999L;

        assertThrows(RuntimeException.class, () -> teacherService.findByIdTeacher(nonExistentId));
    }

    @Test
    void update_ShouldThrowExceptionWhenTeacherNotFound() {
        Long nonExistentId = 999L;
        TeacherRequestDto updateRequest = new TeacherRequestDto();
        updateRequest.setName("Nonexistent");

        assertThrows(RuntimeException.class, () -> teacherService.updateTeacher(nonExistentId, updateRequest));
    }

    @Test
    void delete_ShouldNotThrowExceptionWhenTeacherNotFound() {
        Long nonExistentId = 999L;

        assertDoesNotThrow(() -> teacherService.deleteTeacher(nonExistentId));
    }

    @Test
    void create_ShouldThrowExceptionWhenFirstNameIsBlank() {
        TeacherRequestDto request = new TeacherRequestDto();
        request.setName(null);
        request.setLastName("Doe");

        assertThrows(Exception.class, () -> teacherService.createTeacher(request));
    }

    @Test
    void create_ShouldThrowExceptionWhenLastNameIsBlank() {
        TeacherRequestDto request = new TeacherRequestDto();
        request.setName("John");
        request.setLastName(null);

        assertThrows(Exception.class, () -> teacherService.createTeacher(request));
    }

    @Test
    void update_ShouldHandlePartialUpdate() {
        Teacher teacher = createTestTeacher();
        String originalLastName = teacher.getSurname();
        String originalPatronymic = teacher.getPatronymic();

        TeacherRequestDto updateRequest = new TeacherRequestDto();
        updateRequest.setName("UpdatedOnly");

        TeacherResponseDto result = teacherService.updateTeacher(teacher.getId(), updateRequest);

        assertEquals("UpdatedOnly", result.getName());
        assertEquals(originalLastName, result.getLastName());
        assertEquals(originalPatronymic, result.getPatronymic());
    }

    @Test
    void addSubjectToTeacher_ShouldAddSubject() {
        Teacher teacher = createTestTeacher();
        Subject subject = createTestSubject();

        TeacherResponseDto result = teacherService.addSubjectToTeacher(teacher.getId(), subject.getId());

        assertNotNull(result.getSubjects());
        assertEquals(1, result.getSubjects().size());
        assertEquals(subject.getId(), result.getSubjects().iterator().next().getId());
    }

    @Test
    void addSubjectToTeacher_ShouldThrowWhenTeacherNotFound() {
        Long nonExistentTeacherId = 999L;
        Subject subject = createTestSubject();

        assertThrows(RuntimeException.class, () ->
                teacherService.addSubjectToTeacher(nonExistentTeacherId, subject.getId()));
    }

    @Test
    void addSubjectToTeacher_ShouldThrowWhenSubjectNotFound() {
        Teacher teacher = createTestTeacher();
        Long nonExistentSubjectId = 999L;

        assertThrows(RuntimeException.class, () ->
                teacherService.addSubjectToTeacher(teacher.getId(), nonExistentSubjectId));
    }

    private AtomicLong counter = new AtomicLong(1);

    private Teacher createTestTeacher() {
        Teacher teacher = new Teacher();
        teacher.setName("Teacher_" + counter.getAndIncrement());
        teacher.setSurname("Lastname_" + counter.getAndIncrement());
        teacher.setPatronymic("Midname_" + counter.getAndIncrement());
        return teacherRepository.save(teacher);
    }

    private Subject createTestSubject() {
        Subject subject = new Subject();
        subject.setName("Subject_" + counter.getAndIncrement());
        return subjectRepository.save(subject);
    }
}