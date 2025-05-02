package codereview.school_mate.service;

import codereview.school_mate.dto.ParentRequestDto;
import codereview.school_mate.dto.ParentResponseDto;
import java.util.List;

public interface ParentService {
        ParentResponseDto createParent(ParentRequestDto dto);
        ParentResponseDto findByIdParent(Long id);
        List<ParentResponseDto> findAllParent();
        ParentResponseDto updateParent(Long id, ParentRequestDto dto);
        void deleteParent(Long id);
    }
