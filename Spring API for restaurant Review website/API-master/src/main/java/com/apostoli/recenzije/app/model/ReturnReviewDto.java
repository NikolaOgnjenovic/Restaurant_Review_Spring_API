package com.apostoli.recenzije.app.model;

import java.util.ArrayList;

public record ReturnReviewDto(
        Long id,
        String title,
        String description,
        Long likes,
        Long dislikes,
        Long foodCost,
        String userEmail,
        ArrayList<String> likedBy,
        ArrayList<String> dislikedBy,
        String placeId
) {
}