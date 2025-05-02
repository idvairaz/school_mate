package codereview.school_mate.controller;

import codereview.school_mate.dto.TeacherRequestDto;
import codereview.school_mate.dto.TeacherResponseDto;
import codereview.school_mate.service.TeacherService;
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
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Учителя", description = "Управление данными учителей")
public class TeacherController {
    private final TeacherService teacherService;

    @Operation(summary = "Создать нового учителя", description = "Создает запись о новом учителе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Учитель успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<TeacherResponseDto> createTeacher(@RequestBody TeacherRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(dto));
    }

    @Operation(summary = "Получить учителя по ID", description = "Возвращает данные учителя по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Учитель найден"),
            @ApiResponse(responseCode = "404", description = "Учитель не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> findByIdTeacher(
            @Parameter(description = "ID учителя", required = true) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.findByIdTeacher(id));
    }

    @Operation(summary = "Получить всех учителей", description = "Возвращает список всех учителей")
    @ApiResponse(responseCode = "200", description = "Список учителей успешно получен")
    @GetMapping
    public ResponseEntity<List<TeacherResponseDto>> findAllTeacher() {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.findAllTeacher());
    }

    @Operation(summary = "Обновить данные учителя", description = "Обновляет информацию об учителе по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Учитель не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> updateTeacher(
            @Parameter(description = "ID учителя", required = true) @PathVariable Long id,
            @RequestBody TeacherRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.updateTeacher(id, dto));
    }

    @Operation(summary = "Удалить учителя", description = "Удаляет запись об учителе по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Учитель успешно удален"),
            @ApiResponse(responseCode = "404", description = "Учитель не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(
            @Parameter(description = "ID учителя", required = true) @PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Добавить предмет учителю", description = "Связывает предмет с учителем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Предмет успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Учитель или предмет не найден")
    })
    @PostMapping("/{teacherId}/subjects/{subjectId}")
    public ResponseEntity<TeacherResponseDto> addSubjectTeacher(
            @Parameter(description = "ID учителя", required = true) @PathVariable Long teacherId,
            @Parameter(description = "ID предмета", required = true) @PathVariable Long subjectId) {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.addSubjectToTeacher(teacherId, subjectId));
    }
}