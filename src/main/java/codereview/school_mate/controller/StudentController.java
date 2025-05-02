package codereview.school_mate.controller;

import codereview.school_mate.dto.StudentRequestDto;
import codereview.school_mate.dto.StudentResponseDto;
import codereview.school_mate.service.StudentService;
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
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Ученики", description = "Управление данными учеников")
public class StudentController {
    private final StudentService studentService;

    @Operation(summary = "Создать нового ученика", description = "Создает запись о новом ученике")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ученик успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody StudentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
    }

    @Operation(summary = "Получить ученика по ID", description = "Возвращает данные ученика по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ученик найден"),
            @ApiResponse(responseCode = "404", description = "Ученик не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> findByIdStudent(
            @Parameter(description = "ID ученика", required = true) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.findByIdStudent(id));
    }

    @Operation(summary = "Получить всех учеников", description = "Возвращает список всех учеников")
    @ApiResponse(responseCode = "200", description = "Список учеников успешно получен")
    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> findAllStudent() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.findAllStudent());
    }

    @Operation(summary = "Обновить данные ученика", description = "Обновляет информацию об ученике по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Ученик не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @Parameter(description = "ID ученика", required = true) @PathVariable Long id,
            @RequestBody StudentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(id, dto));
    }

    @Operation(summary = "Удалить ученика", description = "Удаляет запись об ученике по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ученик успешно удален"),
            @ApiResponse(responseCode = "404", description = "Ученик не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID ученика", required = true) @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}