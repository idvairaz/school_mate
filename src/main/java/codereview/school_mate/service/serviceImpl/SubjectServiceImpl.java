package codereview.school_mate.service.serviceImpl;

import codereview.school_mate.dto.SubjectRequestDto;
import codereview.school_mate.dto.SubjectResponseDto;
import codereview.school_mate.mapper.SubjectMapper;
import codereview.school_mate.model.Subject;
import codereview.school_mate.repository.SubjectRepository;
import codereview.school_mate.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    @Transactional
    public SubjectResponseDto createSubject(SubjectRequestDto dto) {
        Subject subject = subjectMapper.toEntity(dto);
        Subject savedSubject = subjectRepository.save(subject);
        return subjectMapper.toDto(savedSubject);
    }

    @Override
    public SubjectResponseDto findByIdSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return subjectMapper.toDto(subject);
    }

    @Override
    public List<SubjectResponseDto> findAllSubject() {
        return subjectMapper.subjectsToSubjectResponseDtos(subjectRepository.findAll());
    }

    @Override
    @Transactional
    public SubjectResponseDto updateSubject(Long id, SubjectRequestDto dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        subjectMapper.updateEntityFromDto(dto, subject);
        return subjectMapper.toDto(subjectRepository.save(subject));
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}