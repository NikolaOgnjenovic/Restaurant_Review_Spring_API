package com.apostoli.recenzije.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(columnDefinition = "integer default 0")
    private Long likes;
    @Column(columnDefinition = "integer default 0")
    private Long dislikes;
    private Long foodCost;
    private String userEmail;
    private ArrayList<String> likedBy;
    private ArrayList<String> dislikedBy;
    private String placeId;
}