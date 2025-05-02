package codereview.school_mate.service;

import codereview.school_mate.dto.SubjectRequestDto;
import codereview.school_mate.dto.SubjectResponseDto;
import java.util.List;

public interface SubjectService {
    SubjectResponseDto createSubject(SubjectRequestDto dto);
    SubjectResponseDto findByIdSubject(Long id);
    List<SubjectResponseDto> findAllSubject();
    SubjectResponseDto updateSubject(Long id, SubjectRequestDto dto);
    void deleteSubject(Long id);
}