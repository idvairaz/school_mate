package codereview.school_mate.service;

import codereview.school_mate.dto.TeacherRequestDto;
import codereview.school_mate.dto.TeacherResponseDto;
import java.util.List;

public interface TeacherService {
    TeacherResponseDto createTeacher(TeacherRequestDto dto);
    TeacherResponseDto findByIdTeacher(Long id);
    List<TeacherResponseDto> findAllTeacher();
    TeacherResponseDto updateTeacher(Long id, TeacherRequestDto dto);
    void deleteTeacher(Long id);
    TeacherResponseDto addSubjectToTeacher(Long teacherId, Long subjectId);
}
