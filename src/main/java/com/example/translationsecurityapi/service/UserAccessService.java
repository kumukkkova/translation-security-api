package com.example.translationsecurityapi.service;

import com.example.translationsecurityapi.entity.Role;
import com.example.translationsecurityapi.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAccessService {

    private final UserRepository userRepository;

    public UserAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean canViewUser(Long userId, Authentication authentication) {

        // Администратор может просматривать любого пользователя
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }

        // Обычный пользователь может просматривать только себя
        return userRepository.findByEmail(authentication.getName())
                .map(user -> user.getId().equals(userId))
                .orElse(false);
    }
}