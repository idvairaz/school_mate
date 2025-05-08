package codereview.school_mate.controller;

import codereview.school_mate.dto.ReportRequestDto;
import codereview.school_mate.dto.ReportResponseDto;
import codereview.school_mate.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
@Tag(name = "Отчёты", description = "Управление данными отчётов")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Создать новый отчёт", description = "Создает запись о новом отчёте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отчёт успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<ReportResponseDto> createReport(@RequestBody ReportRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(dto));
    }


    @Operation(summary = "Получить отчёт по ID", description = "Возвращает данные отчёта по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчёт найден"),
            @ApiResponse(responseCode = "404", description = "Отчёт не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDto> findReportById(
            @Parameter(description = "ID отчёта", required = true) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.findReportById(id));
    }
    @Operation(summary = "Получить все отчёты", description = "Возвращает список всех отчётов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список отчётов успешно получен")
    })
    @GetMapping
    public ResponseEntity<List<ReportResponseDto>> getAllReports () {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getAllReports());
    }

    @Operation(summary = "Обновить данные отчёта", description = "Обновляет информацию об отчёте по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Отчёт не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReportResponseDto> updateReport(
            @Parameter(description = "ID отчёта", required = true) @PathVariable Long id,
            @RequestBody ReportRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.updateReport(id, dto));
    }

    @Operation(summary = "Удалить отчёт", description = "Удаляет запись об отчёте по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Отчёт успешно удален"),
            @ApiResponse(responseCode = "404", description = "Отчёт не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "ID отчёта", required = true) @PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
