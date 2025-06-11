package codereview.school_mate.service.serviceImpl;

import codereview.school_mate.dto.request.registration.AdminRegistrationRequestDto;
import codereview.school_mate.dto.responce.AdminResponseDto;
import codereview.school_mate.mapper.AdminMapper;
import codereview.school_mate.model.Admin;
import codereview.school_mate.model.User;
import codereview.school_mate.repository.AdminRepository;
import codereview.school_mate.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Transactional
    @Override
    public AdminResponseDto createAdmin(AdminRegistrationRequestDto adminRegistrationRequestDto, User user) {
        Admin admin = new Admin();
        admin.setUser(user);
        Admin saved = adminRepository.save(admin);
        return adminMapper.toDto(saved);
    }
}
