package com.example.translationsecurityapi.controller;

import com.example.translationsecurityapi.entity.AppUser;
import com.example.translationsecurityapi.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Map<String, String> getUserInfo(
            Authentication authentication
    ) {
        return Map.of(
                "email", authentication.getName(),
                "message", "Доступ разрешён"
        );
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("@userAccessService.canViewUser(#p0, authentication)")
    public Map<String, Object> getUserById(
            @PathVariable Long id
    ) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Пользователь не найден"
                        )
                );

        return Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "status", user.getStatus().name()
        );
    }

    @GetMapping("/admin/panel")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> getAdminPanel() {
        return Map.of(
                "message", "Добро пожаловать в панель администратора"
        );
    }
}