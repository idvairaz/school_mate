package codereview.school_mate.serviceImpl;

import codereview.school_mate.dto.request.registration.TeacherRegistrationRequestDto;
import codereview.school_mate.dto.request.TeacherRequestDto;
import codereview.school_mate.dto.responce.TeacherResponseDto;
import codereview.school_mate.mapper.TeacherMapper;
import codereview.school_mate.model.Subject;
import codereview.school_mate.model.Teacher;
import codereview.school_mate.model.User;
import codereview.school_mate.repository.SchoolClassRepository;
import codereview.school_mate.repository.SubjectRepository;
import codereview.school_mate.repository.TeacherRepository;
import codereview.school_mate.service.serviceImpl.TeacherServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
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
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
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
        teacherRepository.deleteAll();
        schoolClassRepository.deleteAll();
        subjectRepository.deleteAll();
        counter.set(1);
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

//    @Test
//    void create_ShouldSuccessfullyCreateTeacher() {
//        TeacherRegistrationRequestDto request = new TeacherRegistrationRequestDto();
//        request.setName("John");
//        request.setSurname("Doe");
//        request.setPatronymic("Smith");
//        User user = new User();
//        user.setUsername("teacher");
//        user.setPassword("teacher");
//
//        TeacherResponseDto result = teacherService.createTeacher(request, user);
//
//        assertNotNull(result.getId());
//        assertEquals(request.getName(), result.getName());
//        assertEquals(request.getSurname(), result.getLastName());
//        assertEquals(request.getPatronymic(), result.getPatronymic());
//    }

    @Transactional
    @Test
    void findById_ShouldReturnExistingTeacher() {
        Teacher teacher = createTestTeacher();

        TeacherResponseDto result = teacherService.findByIdTeacher(teacher.getId());

        assertNotNull(result);
        assertEquals(teacher.getId(), result.getId());
        assertEquals(teacher.getName(), result.getName());
        assertEquals(teacher.getSurname(), result.getSurname());
    }

    @Test
    @Transactional
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
        updateRequest.setSurname("Name");
        updateRequest.setPatronymic("Patronymic");

        TeacherResponseDto result = teacherService.updateTeacher(teacher.getId(), updateRequest);

        assertEquals("Updated", result.getName());
        assertEquals("Name", result.getSurname());
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
        TeacherRegistrationRequestDto request = new TeacherRegistrationRequestDto();
        request.setName(null);
        request.setSurname("Doe");
        User user = new User();
        user.setUsername("student");
        user.setPassword("student");

        assertThrows(Exception.class, () -> teacherService.createTeacher(request, user));
    }

    @Test
    void create_ShouldThrowExceptionWhenLastNameIsBlank() {
        TeacherRegistrationRequestDto request = new TeacherRegistrationRequestDto();
        request.setName("John");
        request.setSurname(null);
        User user = new User();
        user.setUsername("student");
        user.setPassword("student");

        assertThrows(Exception.class, () -> teacherService.createTeacher(request, user));
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
        assertEquals(originalLastName, result.getSurname());
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
        teacher.setSurname("Surname_" + counter.getAndIncrement());
        teacher.setPatronymic("Patronymic_" + counter.getAndIncrement());

        return teacherRepository.save(teacher);
    }

    private Subject createTestSubject() {
        Subject subject = new Subject();
        subject.setName("Subject_" + counter.getAndIncrement());
        return subjectRepository.save(subject);
    }
}