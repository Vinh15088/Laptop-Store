package com.LaptopWeb.controller;

import com.LaptopWeb.dto.request.ReviewRequest;
import com.LaptopWeb.dto.response.ApiResponse;
import com.LaptopWeb.dto.response.ReviewResponse;
import com.LaptopWeb.entity.Review;
import com.LaptopWeb.mapper.ReviewMapper;
import com.LaptopWeb.service.ReviewService;
import com.LaptopWeb.utils.PageInfo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private static final String SLICE_NUMBER = "1";
    private static final String SLICE_SIZE = "5";

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest request, @AuthenticationPrincipal Jwt jwt) {
        Review review = reviewService.createReview(jwt.getSubject(), request);

        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(reviewResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable("reviewId") Integer reviewId) {
        Review review = reviewService.getReviewById(reviewId);

        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(reviewResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/{productId}/byRating")
    public ResponseEntity<?> getReviewByRating(
            @PathVariable(name = "productId") Integer productId,
            @RequestParam(name = "rating", required = false) Integer rating,
            @RequestParam(name = "number", defaultValue = SLICE_NUMBER) Integer number,
            @RequestParam(name = "size", defaultValue = SLICE_SIZE) Integer size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "order", defaultValue = "desc") String order
    ) {
        Slice<Review> slice = reviewService.getByRatingAndProduct(rating, productId, number-1, size, sortBy, order);

        PageInfo pageInfo = PageInfo.builder()
                .page(slice.getNumber() + 1)
                .size(slice.getSize())
                .totalElements(slice.getNumberOfElements())
                .hasNext(slice.hasNext())
                .build();

        List<Review> reviews = slice.getContent();

        List<ReviewResponse> reviewResponses = reviews.stream().map(reviewMapper::toReviewResponse).toList();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(reviewResponses)
                .pageInfo(pageInfo)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("{productId}/byUser")
    public ResponseEntity<?> getReviewByUser(
            @PathVariable(name = "productId") Integer productId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Review review = reviewService.getByUserAndProduct(jwt.getSubject(), productId);

        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(reviewResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable(name = "reviewId") Integer reviewId,
            @RequestBody ReviewRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Review review = reviewService.updateReview(reviewId, request, jwt.getSubject());

        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content(reviewResponse)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(name = "reviewId") Integer reviewId) {
        reviewService.deleteReview(reviewId);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .content("Delete review successful")
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

}
