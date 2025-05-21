package codereview.school_mate.service;

import codereview.school_mate.dto.ReportRequestDto;
import codereview.school_mate.dto.ReportResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ReportService {
    ReportResponseDto createReport(ReportRequestDto reportResponseDto);
    ReportResponseDto findReportById(Long id);
    List<ReportResponseDto> getAllReports();
    ReportResponseDto updateReport(Long id, ReportRequestDto reportDto);
    void deleteReport(Long id);
}

