package com.apostoli.recenzije.app.controller;

import com.apostoli.recenzije.app.model.CreateReviewDto;
import com.apostoli.recenzije.app.model.ReturnReviewDto;
import com.apostoli.recenzije.app.model.Review;
import com.apostoli.recenzije.app.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class ReviewController {

    @RequestMapping(method = RequestMethod.OPTIONS)
    ResponseEntity<?> options() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE)
                .build();
    }

    private final ReviewService reviewService;

    @GetMapping
    List<ReturnReviewDto> getAllReviews() {
        log.info("Getting all reviews");
        return reviewService.getAllReviews();
    }

    @GetMapping("/getReviewsByPlaceId/{placeId}")
    List<ReturnReviewDto> getReviewsByPlaceId(@PathVariable String placeId) {
        return reviewService.getReviewsByPlaceId(placeId);
    }

    @GetMapping("/{id}")
    ReturnReviewDto getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping
    ReturnReviewDto createReview(@RequestBody @Valid CreateReviewDto review) {
        Review createReview = Review.builder()
                .title(review.title())
                .description(review.description())
                .foodCost(review.foodCost())
                .userEmail(review.userEmail())
                .placeId(review.placeId())
                .likes(0L)
                .dislikes(0L)
                .build();
        return reviewService.createReview(createReview);
    }

    @PutMapping("/{id}")
    ReturnReviewDto updateReview(@PathVariable Long id, @RequestBody Review ReviewUpdated) {
        return reviewService.updateReviewById(id, ReviewUpdated);
    }

    @PutMapping("/{id}/likeReview")
    ReturnReviewDto likeReview(@PathVariable Long id, @RequestBody String userEmail) {
        return reviewService.likeReviewById(id, userEmail);
    }

    @PutMapping("/{id}/dislikeReview")
    ReturnReviewDto dislikeReview(@PathVariable Long id, @RequestBody String userEmail) {
        return reviewService.dislikeReviewById(id, userEmail);
    }

    @DeleteMapping("{id}/deleteReview")
    void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}