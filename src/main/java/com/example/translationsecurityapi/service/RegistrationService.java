package com.example.translationsecurityapi.service;

import com.example.translationsecurityapi.dto.RegistrationRequest;
import com.example.translationsecurityapi.dto.RegistrationResponse;
import com.example.translationsecurityapi.entity.AccountStatus;
import com.example.translationsecurityapi.entity.AppUser;
import com.example.translationsecurityapi.entity.Role;
import com.example.translationsecurityapi.exception.EmailAlreadyExistsException;
import com.example.translationsecurityapi.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(
                    "Пользователь с таким email уже зарегистрирован"
            );
        }

        AppUser user = new AppUser();

        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.USER);
        user.setStatus(AccountStatus.ACTIVE);

        AppUser savedUser = userRepository.save(user);

        return new RegistrationResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                "Регистрация успешно завершена"
        );
    }
}