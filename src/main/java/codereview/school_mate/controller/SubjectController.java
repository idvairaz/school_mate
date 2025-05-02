package codereview.school_mate.controller;

import codereview.school_mate.dto.SubjectRequestDto;
import codereview.school_mate.dto.SubjectResponseDto;
import codereview.school_mate.service.SubjectService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Tag(name = "Предметы", description = "Управление данными учебных предметов")
public class SubjectController {
    private final SubjectService subjectService;

    @Operation(summary = "Создать новый предмет", description = "Создает запись о новом учебном предмете")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Предмет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<SubjectResponseDto> createSubject(@RequestBody SubjectRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.createSubject(dto));
    }

    @Operation(summary = "Получить предмет по ID", description = "Возвращает данные предмета по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Предмет найден"),
            @ApiResponse(responseCode = "404", description = "Предмет не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> findByIdSubject(
            @Parameter(description = "ID предмета", required = true) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.findByIdSubject(id));
    }

    @Operation(summary = "Получить все предметы", description = "Возвращает список всех учебных предметов")
    @ApiResponse(responseCode = "200", description = "Список предметов успешно получен")
    @GetMapping
    public ResponseEntity<List<SubjectResponseDto>> findAllSubject() {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.findAllSubject());
    }

    @Operation(summary = "Обновить данные предмета", description = "Обновляет информацию о предмете по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Предмет не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> updateSubject(
            @Parameter(description = "ID предмета", required = true) @PathVariable Long id,
            @RequestBody SubjectRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.updateSubject(id, dto));
    }

    @Operation(summary = "Удалить предмет", description = "Удаляет запись о предмете по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Предмет успешно удален"),
            @ApiResponse(responseCode = "404", description = "Предмет не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(
            @Parameter(description = "ID предмета", required = true) @PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}