package codereview.school_mate.service;

import codereview.school_mate.dto.StudentRequestDto;
import codereview.school_mate.dto.StudentResponseDto;
import java.util.List;

public interface StudentService {
        StudentResponseDto createStudent(StudentRequestDto dto);
        StudentResponseDto findByIdStudent(Long id);
        List<StudentResponseDto> findAllStudent();
        StudentResponseDto updateStudent(Long id, StudentRequestDto dto);
        void deleteStudent(Long id);
    }