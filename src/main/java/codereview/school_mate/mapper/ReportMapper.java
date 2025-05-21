package codereview.school_mate.mapper;

import codereview.school_mate.dto.ReportRequestDto;
import codereview.school_mate.dto.ReportResponseDto;
import codereview.school_mate.model.Report;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "date", ignore = true)
    Report toEntity(ReportRequestDto dto);

    ReportResponseDto toDto(Report entity);

//    ReportResponseDto reportToReportResponseDto(Report entity);

    List<ReportResponseDto> toDtos(List<Report> reports);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "date", ignore = true)
    void updateEntityFromDto(ReportRequestDto dto, @MappingTarget Report entity);
}