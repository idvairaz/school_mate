package codereview.school_mate.serviceImpl;

import codereview.school_mate.dto.ParentRequestDto;
import codereview.school_mate.dto.ParentResponseDto;
import codereview.school_mate.mapper.ParentMapper;
import codereview.school_mate.model.Parent;
import codereview.school_mate.repository.ParentRepository;
import codereview.school_mate.service.serviceImpl.ParentServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Testcontainers
class ParentServiceTest {

    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private ParentMapper parentMapper;
    @Autowired
    private ParentServiceImpl parentService;

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
        parentRepository.deleteAll();
    }
    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    @Test
    void create_ValidRequest_ReturnsParentResponseDto() {
        ParentRequestDto request = createValidParentRequest();

        ParentResponseDto result = parentService.createParent(request);

        assertNotNull(result.getId());
        assertEquals("John", result.getName());
        assertEquals(1, parentRepository.count());
    }

    @Test
    void create_MissingContacts_ThrowsException() {
        ParentRequestDto request = createValidParentRequest();
        request.setContacts(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            parentService.createParent(request);
        });
    }

    @Test
    @Transactional
    void findById_ExistingId_ReturnsParent() {
        Parent parent = new Parent();
        parent.setName("Test");
        parent.setSurname("Parent");
        parent.setPatronymic("Testovich");
        parent.setContacts("test@test.com");
        parent = parentRepository.save(parent);

        ParentResponseDto result = parentService.findByIdParent(parent.getId());

        assertEquals(parent.getId(), result.getId());
        assertEquals("Test", result.getName());
    }

    @Test
    void findById_NonExistingId_ThrowsException() {
        assertThrows(RuntimeException.class, () -> parentService.findByIdParent(999L));
    }

    private ParentRequestDto createValidParentRequest() {
        ParentRequestDto request = new ParentRequestDto();
        request.setName("John");
        request.setSurname("Doe");
        request.setPatronymic("Smith");
        request.setContacts("john@example.com");
        return request;
    }
}