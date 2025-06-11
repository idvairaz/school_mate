package codereview.school_mate.mapper;

import codereview.school_mate.dto.request.registration.ParentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.RegistrationRequestDto;
import codereview.school_mate.dto.request.registration.StudentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.TeacherRegistrationRequestDto;
import codereview.school_mate.dto.responce.UserResponseDto;
import codereview.school_mate.model.User;
import codereview.school_mate.dto.request.registration.AdminRegistrationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegistrationRequestDto studentDtoToRegistrationDto(StudentRegistrationRequestDto studentRegistrationRequestDto);
  
    RegistrationRequestDto parentDtoToRegistrationDto(ParentRegistrationRequestDto parentRegistrationRequestDto);

    RegistrationRequestDto teacherDtoToRegistrationDto(TeacherRegistrationRequestDto teacherRegistrationRequestDto);
  
    RegistrationRequestDto adminDtoToRegistrationDto(AdminRegistrationRequestDto adminRegistrationRequestDto);

    @Mapping(target = "role", source = "role.name")
    UserResponseDto userToUserResponseDto(User user);
}
