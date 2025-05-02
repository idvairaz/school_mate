package codereview.school_mate.mapper;

import codereview.school_mate.dto.StudentRequestDto;
import codereview.school_mate.dto.StudentResponseDto;
import codereview.school_mate.model.Student;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    Student toEntity(StudentRequestDto dto);

    StudentResponseDto studentToStudentResponseDto(Student entity);
    List<StudentResponseDto> studentsToStudentResponseDtos(List<Student> students);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    void updateEntityFromDto(StudentRequestDto dto, @MappingTarget Student entity);
}
