package codereview.school_mate.controller;

import codereview.school_mate.dto.SubjectResponseDto;
import codereview.school_mate.dto.TeacherRequestDto;
import codereview.school_mate.dto.TeacherResponseDto;
import codereview.school_mate.service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void create_ShouldReturnCreatedTeacher() throws Exception {
        TeacherRequestDto requestDto = new TeacherRequestDto();
        requestDto.setName("John");
        requestDto.setLastName("Doe");
        requestDto.setPatronymic("Smith");

        TeacherResponseDto responseDto = new TeacherResponseDto();
        responseDto.setId(1L);
        responseDto.setName("John");
        responseDto.setLastName("Doe");
        responseDto.setPatronymic("Smith");

        when(teacherService.createTeacher(any(TeacherRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void findById_ShouldReturnTeacher() throws Exception {
        TeacherResponseDto responseDto = new TeacherResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Dmitri");
        responseDto.setLastName("Smirnov");
        responseDto.setPatronymic("Olegovich");

        when(teacherService.findByIdTeacher(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/teachers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dmitri"))
                .andExpect(jsonPath("$.lastName").value("Smirnov"));
    }

    @Test
    void update_ShouldReturnUpdatedTeacher() throws Exception {
        TeacherRequestDto requestDto = new TeacherRequestDto();
        requestDto.setName("Updated");
        requestDto.setLastName("Teacher");
        requestDto.setPatronymic("Patronymic");

        TeacherResponseDto responseDto = new TeacherResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Updated");
        responseDto.setLastName("Teacher");
        responseDto.setPatronymic("Patronymic");

        when(teacherService.updateTeacher(eq(1L), any(TeacherRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/teachers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void addSubject_ShouldReturnTeacherWithSubject() throws Exception {
        SubjectResponseDto subjectDto = new SubjectResponseDto();
        subjectDto.setId(1L);
        subjectDto.setName("Math");

        TeacherResponseDto responseDto = new TeacherResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Ivan");
        responseDto.setLastName("Ivanov");
        responseDto.setSubjects(Set.of(subjectDto));

        when(teacherService.addSubjectToTeacher(1L, 1L)).thenReturn(responseDto);

        mockMvc.perform(post("/api/teachers/{teacherId}/subjects/{subjectId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subjects[0].name").value("Math"));
    }

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
}