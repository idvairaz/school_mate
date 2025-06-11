package codereview.school_mate.mapper;

import codereview.school_mate.dto.responce.AdminResponseDto;
import codereview.school_mate.model.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminResponseDto toDto(Admin admin);
}
