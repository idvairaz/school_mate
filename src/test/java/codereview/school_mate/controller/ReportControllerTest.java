package codereview.school_mate.controller;

import codereview.school_mate.dto.ReportRequestDto;
import codereview.school_mate.dto.ReportResponseDto;
import codereview.school_mate.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    // Уже есть createReport_shouldReturnCreated()

    @Test
    void findReportById_shouldReturnReport() throws Exception {
        ReportResponseDto responseDto = new ReportResponseDto();
        responseDto.setId(1L);
        responseDto.setMessage("Test report");
        // остальные поля можешь заполнить при необходимости

        when(reportService.findReportById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/reports/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("Test report"));
    }

    @Test
    void getAllReports_shouldReturnList() throws Exception {
        ReportResponseDto dto1 = new ReportResponseDto();
        dto1.setId(1L);
        dto1.setMessage("Report 1");

        ReportResponseDto dto2 = new ReportResponseDto();
        dto2.setId(2L);
        dto2.setMessage("Report 2");

        List<ReportResponseDto> list = List.of(dto1, dto2);
        when(reportService.getAllReports()).thenReturn(list);

        mockMvc.perform(get("/api/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].message").value("Report 2"));
    }

    @Test
    void updateReport_shouldReturnUpdated() throws Exception {
        ReportRequestDto requestDto = new ReportRequestDto();
        requestDto.setMessage("Updated message");

        ReportResponseDto responseDto = new ReportResponseDto();
        responseDto.setId(1L);
        responseDto.setMessage("Updated message");

        when(reportService.updateReport(eq(1L), any(ReportRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/reports/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("Updated message"));
    }

    @Test
    void deleteReport_shouldReturnNoContent() throws Exception {
        // deleteReport возвращает void, поэтому только проверяем статус
        doNothing().when(reportService).deleteReport(1L);

        mockMvc.perform(delete("/api/reports/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
