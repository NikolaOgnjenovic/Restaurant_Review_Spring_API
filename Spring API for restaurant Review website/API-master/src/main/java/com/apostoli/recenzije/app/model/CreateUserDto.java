package com.apostoli.recenzije.app.model;

import javax.validation.constraints.NotBlank;

public record CreateUserDto(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String email
) {
}