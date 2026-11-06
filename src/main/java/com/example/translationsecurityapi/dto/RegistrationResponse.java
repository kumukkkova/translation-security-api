package com.example.translationsecurityapi.dto;

public class RegistrationResponse {

    private Long id;
    private String email;
    private String message;

    public RegistrationResponse(Long id, String email, String message) {
        this.id = id;
        this.email = email;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }
}