package com.apostoli.recenzije.app.model;

import javax.validation.constraints.NotBlank;

public record CreateReviewDto(
        @NotBlank
        String title,
        @NotBlank
        String description,
        Long foodCost,
        String userEmail,
        String placeId
) {
}