package codereview.school_mate.controller;

import codereview.school_mate.dto.request.JwtRequest;
import codereview.school_mate.dto.request.registration.AdminRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.ParentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.StudentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.TeacherRegistrationRequestDto;
import codereview.school_mate.dto.responce.JwtResponse;
import codereview.school_mate.dto.responce.ParentResponseDto;
import codereview.school_mate.dto.responce.StudentResponseDto;
import codereview.school_mate.dto.responce.TeacherResponseDto;
import codereview.school_mate.dto.responce.AdminResponseDto;
import codereview.school_mate.dto.responce.UserResponseDto;
import codereview.school_mate.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Аутентификация и авторизация пользователей")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Зайти в учетную запись", description = "Выполняет вход в учетную запись. Возвращает access и refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Вход выполнен успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> createAuthToken(@Valid @RequestBody JwtRequest authRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.createAuthToken(authRequest));
    }

    @Operation(summary = "Обновить access и refresh tokens", description = "Выдает новые токены доступа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Токены успешно сгенерированы"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.refreshToken(request));
    }

    @Operation(summary = "Создать нового ученика", description = "Создает запись о новом ученике")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ученик успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PostMapping("/signup/student")
    public ResponseEntity<StudentResponseDto> createNewStudent(@Valid @RequestBody StudentRegistrationRequestDto studentRegistrationRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createNewStudent(studentRegistrationRequestDto));
    }

    @Operation(summary = "Создать нового родителя", description = "Создает запись о новом родителе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Родитель успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PostMapping("/signup/parent")
    public ResponseEntity<ParentResponseDto> createNewParent(@Valid @RequestBody ParentRegistrationRequestDto parentRegistrationRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createNewParent(parentRegistrationRequestDto));
    }

    @Operation(summary = "Создать нового учителя", description = "Создает запись о новом учителе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Учитель успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PostMapping("/signup/teacher")
    public ResponseEntity<TeacherResponseDto> createNewTeacher(@Valid @RequestBody TeacherRegistrationRequestDto teacherRegistrationRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createNewTeacher(teacherRegistrationRequestDto));
    }

    @Operation(summary = "Получить данные пользователя", description = "Выдает данные пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о пользователе предоставлены"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUser(Principal principal){
        log.info("GET /auth/user username = {}", principal.getName());
        return authService.getUser(principal.getName());
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<AdminResponseDto> createNewAdmin(@Valid @RequestBody AdminRegistrationRequestDto adminRegistrationRequestDto) {
        return ResponseEntity.ok(authService.createNewAdministration(adminRegistrationRequestDto));
    }
}
