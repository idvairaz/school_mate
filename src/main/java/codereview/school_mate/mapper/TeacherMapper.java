package codereview.school_mate.mapper;

import codereview.school_mate.dto.SubjectResponseDto;
import codereview.school_mate.dto.TeacherRequestDto;
import codereview.school_mate.dto.TeacherResponseDto;
import codereview.school_mate.model.Subject;
import codereview.school_mate.model.Teacher;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SubjectMapper.class})
public interface TeacherMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "surname", source = "lastName")
    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "classes", ignore = true)
    Teacher toEntity(TeacherRequestDto dto);

    @Mapping(target = "lastName", source = "surname")
    TeacherResponseDto toDto(Teacher entity);

    List<TeacherResponseDto> teachersToTeacherResponseDtos(List<Teacher> teachers);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "classes", ignore = true)
    @Mapping(target = "surname", source = "lastName")
    void updateEntityFromDto(TeacherRequestDto dto, @MappingTarget Teacher entity);
}