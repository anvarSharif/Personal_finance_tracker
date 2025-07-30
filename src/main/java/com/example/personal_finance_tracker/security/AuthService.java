package com.example.personal_finance_tracker.security;

import com.example.personal_finance_tracker.constants.ReportMessages;
import com.example.personal_finance_tracker.dto.AuthResponseDTO;
import com.example.personal_finance_tracker.dto.LoginDTO;
import com.example.personal_finance_tracker.dto.RegisterDTO;
import com.example.personal_finance_tracker.dto.RegisterResponseDTO;
import com.example.personal_finance_tracker.entity.UserEntity;
import com.example.personal_finance_tracker.repo.UserEntityRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;

    public AuthService(TokenService tokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                       UserEntityRepository userEntityRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userEntityRepository = userEntityRepository;
    }

    public AuthResponseDTO handleLogin(LoginDTO loginDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return new AuthResponseDTO(tokenService.generateToken((UserEntity) authenticate.getPrincipal()));
    }

    public RegisterResponseDTO saveUser(RegisterDTO registerDTO) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByEmail(registerDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new RuntimeException(ReportMessages.ERROR_EMAIL_EXISTS);
        }
        UserEntity userEntity = UserEntity.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .fullName(registerDTO.getFullName())
                .createdAt(LocalDateTime.now())
                .build();
        userEntityRepository.save(userEntity);
        return RegisterResponseDTO.builder()
                .id(userEntity.getId())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .build();
    }
}
