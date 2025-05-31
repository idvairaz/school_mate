package codereview.school_mate.service.serviceImpl;

import codereview.school_mate.dto.request.JwtRequest;
import codereview.school_mate.dto.request.registration.ParentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.StudentRegistrationRequestDto;
import codereview.school_mate.dto.request.registration.TeacherRegistrationRequestDto;
import codereview.school_mate.dto.responce.*;
import codereview.school_mate.exception.IncorrectUsernameException;
import codereview.school_mate.exception.JwtTokenException;
import codereview.school_mate.exception.NotFoundException;
import codereview.school_mate.mapper.UserMapper;
import codereview.school_mate.model.User;
import codereview.school_mate.service.AuthService;
import codereview.school_mate.service.TeacherService;
import codereview.school_mate.service.ParentService;
import codereview.school_mate.service.StudentService;
import codereview.school_mate.service.UserService;
import codereview.school_mate.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Map<String, String> refreshStorage = new HashMap<>();
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final StudentService studentService;
    private final ParentService parentService;
    private final TeacherService teacherService;
    private final UserMapper userMapper;

    @Override
    public JwtResponse createAuthToken(JwtRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

        String accessToken = jwtTokenUtils.generateAccessToken(userDetails);
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);

        refreshStorage.put(userDetails.getUsername(), refreshToken);

        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String refreshToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);
        }

        try {
            String username = jwtTokenUtils.getUsername(refreshToken);
            String savedRefreshToken = refreshStorage.get(username);

            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                User user = userService.findByUsername(username)
                        .orElseThrow(() -> new NotFoundException("User with username = " + username + " not found"));
                String accessToken = jwtTokenUtils.generateAccessToken(user);
                String newRefreshToken = jwtTokenUtils.generateRefreshToken(user);

                refreshStorage.put(user.getUsername(), newRefreshToken);

                return new JwtResponse(accessToken, newRefreshToken);
            }
        } catch (ExpiredJwtException ex) {
            log.error("Token expired: {}", ex.getMessage());
            throw new JwtTokenException(ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT: {}", ex.getMessage());
            throw new JwtTokenException(ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Malformed JWT: {}", ex.getMessage());
            throw new JwtTokenException(ex.getMessage());
        } catch (SignatureException ex) {
            log.error("Invalid signature: {}", ex.getMessage());
            throw new JwtTokenException(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
            throw new JwtTokenException(ex.getMessage());
        }
        return new JwtResponse(null, null);
    }

    @Transactional
    @Override
    public StudentResponseDto createNewStudent(StudentRegistrationRequestDto studentRegistrationRequestDto) {
        if (userService.findByUsername(studentRegistrationRequestDto.getUsername()).isPresent()) {
            throw new IncorrectUsernameException("User with the specified name already exists");
        }
        User user = userService.create(userMapper.studentDtoToRegistrationDto(studentRegistrationRequestDto));

        return studentService.createStudent(studentRegistrationRequestDto, user);
    }

    @Transactional
    @Override
    public ParentResponseDto createNewParent(ParentRegistrationRequestDto parentRegistrationRequestDto) {
        if (userService.findByUsername(parentRegistrationRequestDto.getUsername()).isPresent()) {
            throw new IncorrectUsernameException("User with the specified name already exists");
        }
        User user = userService.create(userMapper.parentDtoToRegistrationDto(parentRegistrationRequestDto));

        return parentService.createParent(parentRegistrationRequestDto, user);
    }

    @Transactional
    @Override
    public TeacherResponseDto createNewTeacher(TeacherRegistrationRequestDto teacherRegistrationRequestDto) {
        if (userService.findByUsername(teacherRegistrationRequestDto.getUsername()).isPresent()) {
            throw new IncorrectUsernameException("User with the specified name already exists");
        }
        User user = userService.create(userMapper.teacherDtoToRegistrationDto(teacherRegistrationRequestDto));

        return teacherService.createTeacher(teacherRegistrationRequestDto, user);
    }

    @Override
    public UserResponseDto getUser(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username = " + username + " not found"));
        log.info("user = {}", user);
        return userMapper.userToUserResponseDto(user);
    }
}
