package codereview.school_mate.service;

import codereview.school_mate.dto.request.registration.AdminRegistrationRequestDto;
import codereview.school_mate.dto.responce.AdminResponseDto;
import codereview.school_mate.model.User;

public interface AdminService {
    AdminResponseDto createAdmin(AdminRegistrationRequestDto adminRegistrationRequestDto, User user);
}
