package codereview.school_mate.mapper;

import codereview.school_mate.dto.SubjectRequestDto;
import codereview.school_mate.dto.SubjectResponseDto;
import codereview.school_mate.model.Subject;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    @Mapping(target = "id", ignore = true)
    Subject toEntity(SubjectRequestDto dto);

    SubjectResponseDto toDto(Subject entity);
    List<SubjectResponseDto> subjectsToSubjectResponseDtos(List<Subject> subjects);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(SubjectRequestDto dto, @MappingTarget Subject entity);
}