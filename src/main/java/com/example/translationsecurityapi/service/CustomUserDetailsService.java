package com.example.translationsecurityapi.service;

import com.example.translationsecurityapi.entity.AppUser;
import com.example.translationsecurityapi.repository.UserRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Пользователь не найден: " + email
                        )
                );

        return User.builder()
                .username(appUser.getEmail())
                .password(appUser.getPasswordHash())
                .roles(appUser.getRole().name())
                .disabled(appUser.getStatus() !=
                        com.example.translationsecurityapi.entity.AccountStatus.ACTIVE)
                .build();
    }
}