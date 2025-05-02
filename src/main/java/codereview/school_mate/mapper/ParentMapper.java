package codereview.school_mate.mapper;

import codereview.school_mate.dto.ParentRequestDto;
import codereview.school_mate.dto.ParentResponseDto;
import codereview.school_mate.model.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ParentMapper {
    Parent toEntity(ParentRequestDto dto);
    ParentResponseDto toDto(Parent entity);
    void updateEntityFromDto(ParentRequestDto dto, @MappingTarget Parent entity);

    List<ParentResponseDto> toDtos(List<Parent> parents);
}
