package codereview.school_mate.controller;

import codereview.school_mate.config.JwtRequestFilter;
import codereview.school_mate.dto.request.TeacherRequestDto;
import codereview.school_mate.dto.request.registration.ParentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.StudentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.TeacherRegistrationRequestDto;
import codereview.school_mate.dto.responce.ParentResponseDto;
import codereview.school_mate.dto.responce.StudentResponseDto;
import codereview.school_mate.dto.responce.TeacherResponseDto;
import codereview.school_mate.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        value = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtRequestFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AuthService authService;

    @Test
    void create_ShouldReturnCreatedParent() throws Exception {
        ParentResponseDto parent = new ParentResponseDto();
        parent.setName("parent");
        ParentRegistrationRequestDto parentRegistrationRequestDto = new ParentRegistrationRequestDto();
        parentRegistrationRequestDto.setName("parent");

        when(authService.createNewParent(any(ParentRegistrationRequestDto.class))).thenReturn(parent);


        mockMvc.perform(post("/auth/signup/parent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parentRegistrationRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(parent.getName()));
    }

    @Test
    void create_ShouldReturnCreatedStudent() throws Exception {
        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setName("student");

        when(authService.createNewStudent(any(StudentRegistrationRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/signup/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new StudentResponseDto()
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(responseDto.getName()));
    }

    @Test
    void create_ShouldReturnCreatedTeacher() throws Exception {
        TeacherResponseDto responseDto = new TeacherResponseDto();
        responseDto.setId(1L);
        responseDto.setName("John");
        responseDto.setSurname("Doe");
        responseDto.setPatronymic("Smith");

        when(authService.createNewTeacher(any(TeacherRegistrationRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/signup/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TeacherRequestDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
}
