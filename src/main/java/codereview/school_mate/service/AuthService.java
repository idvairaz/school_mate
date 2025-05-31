package codereview.school_mate.service;

import codereview.school_mate.dto.request.JwtRequest;
import codereview.school_mate.dto.request.registration.ParentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.StudentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.TeacherRegistrationRequestDto;
import codereview.school_mate.dto.responce.*;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    JwtResponse createAuthToken(JwtRequest authRequest);
    JwtResponse refreshToken(HttpServletRequest request);

    StudentResponseDto createNewStudent(StudentRegistrationRequestDto studentRegistrationRequestDto);
    ParentResponseDto createNewParent(ParentRegistrationRequestDto parentRegistrationRequestDto);
    TeacherResponseDto createNewTeacher(TeacherRegistrationRequestDto teacherRegistrationRequestDto);
    UserResponseDto getUser(String username);

}
