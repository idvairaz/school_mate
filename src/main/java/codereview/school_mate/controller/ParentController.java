package codereview.school_mate.controller;

import codereview.school_mate.dto.ParentRequestDto;
import codereview.school_mate.dto.ParentResponseDto;
import codereview.school_mate.service.ParentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/parents")
@RequiredArgsConstructor
@Tag(name = "Родители", description = "Управление данными родителей")
public class ParentController {
    private final ParentService parentService;

    @Operation(summary = "Создать нового родителя", description = "Создает запись о новом родителе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Родитель успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<ParentResponseDto> createParent(@RequestBody ParentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parentService.createParent(dto));
    }

    @Operation(summary = "Получить родителя по ID", description = "Возвращает данные родителя по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Родитель найден"),
            @ApiResponse(responseCode = "404", description = "Родитель не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ParentResponseDto> findByIdParent(
            @Parameter(description = "ID родителя", required = true) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(parentService.findByIdParent(id));
    }

    @Operation(summary = "Получить всех родителей", description = "Возвращает список всех родителей")
    @ApiResponse(responseCode = "200", description = "Список родителей успешно получен")
    @GetMapping
    public ResponseEntity<List<ParentResponseDto>> findAllParent() {
        return ResponseEntity.status(HttpStatus.OK).body(parentService.findAllParent());
    }

    @Operation(summary = "Обновить данные родителя", description = "Обновляет информацию о родителе по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Родитель не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ParentResponseDto> updateParent(
            @Parameter(description = "ID родителя", required = true) @PathVariable Long id,
            @RequestBody ParentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(parentService.updateParent(id, dto));
    }

    @Operation(summary = "Удалить родителя", description = "Удаляет запись о родителе по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Родитель успешно удален"),
            @ApiResponse(responseCode = "404", description = "Родитель не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParent(
            @Parameter(description = "ID родителя", required = true) @PathVariable Long id) {
        parentService.deleteParent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
