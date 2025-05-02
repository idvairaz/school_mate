package codereview.school_mate.controller;

import codereview.school_mate.dto.SubjectRequestDto;
import codereview.school_mate.dto.SubjectResponseDto;
import codereview.school_mate.service.SubjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(SubjectController.class)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_ShouldReturnCreatedSubject() throws Exception {
        SubjectResponseDto responseDto = new SubjectResponseDto(1L, "Mathematics");

        when(subjectService.createSubject(any(SubjectRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubjectRequestDto("Mathematics")
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mathematics"));
    }

    @Test
    void findById_ShouldReturnSubject() throws Exception {
        SubjectResponseDto responseDto = new SubjectResponseDto(1L, "Physics");

        when(subjectService.findByIdSubject(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/subjects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Physics"));
    }

    @Test
    void findAll_ShouldReturnSubjectsList() throws Exception {
        List<SubjectResponseDto> subjects = List.of(
                new SubjectResponseDto(1L, "Math"),
                new SubjectResponseDto(2L, "Physics")
        );

        when(subjectService.findAllSubject()).thenReturn(subjects);

        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name").value("Physics"));
    }

    @Test
    void update_ShouldReturnUpdatedSubject() throws Exception {
        SubjectResponseDto responseDto = new SubjectResponseDto(1L, "Updated Math");

        when(subjectService.updateSubject(eq(1L), any(SubjectRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/subjects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubjectRequestDto("Math")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Math"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(subjectService).deleteSubject(1L);

        mockMvc.perform(delete("/api/subjects/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(subjectService, times(1)).deleteSubject(1L);
    }
}