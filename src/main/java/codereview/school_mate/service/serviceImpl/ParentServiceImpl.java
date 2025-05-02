package codereview.school_mate.service.serviceImpl;

import codereview.school_mate.dto.ParentRequestDto;
import codereview.school_mate.dto.ParentResponseDto;
import codereview.school_mate.mapper.ParentMapper;
import codereview.school_mate.model.Parent;
import codereview.school_mate.repository.ParentRepository;
import codereview.school_mate.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;

    @Override
    public ParentResponseDto createParent(ParentRequestDto dto) {
        Parent parent = parentMapper.toEntity(dto);
        Parent savedParent = parentRepository.save(parent);
        return parentMapper.toDto(savedParent);
    }

    @Override
    public ParentResponseDto findByIdParent(Long id) {
        Parent parent = parentRepository.findById(id).orElseThrow(()-> new RuntimeException("Parent not found with id: " + id));
        return parentMapper.toDto(parent);
    }

    @Override
    public List<ParentResponseDto> findAllParent() {
        return parentMapper.toDtos(parentRepository.findAll());
    }

    @Override
    @Transactional
    public ParentResponseDto updateParent(Long id, ParentRequestDto dto) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + id));
        parentMapper.updateEntityFromDto(dto, parent);
        return parentMapper.toDto(parentRepository.save(parent));
    }

    @Override
    public void deleteParent(Long id) {
        parentRepository.deleteById(id);
    }
}
