package com.LaptopWeb.service;

import com.LaptopWeb.dto.request.ReviewRequest;
import com.LaptopWeb.entity.Product;
import com.LaptopWeb.entity.Review;
import com.LaptopWeb.entity.User;
import com.LaptopWeb.exception.AppException;
import com.LaptopWeb.exception.ErrorApp;
import com.LaptopWeb.mapper.ReviewMapper;
import com.LaptopWeb.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public Review createReview(String username, ReviewRequest request) {
        User user = userService.getByUsername(username);

        Product product = productService.getProductById(request.getProduct_id());

        if(reviewRepository.existsByUserAndProduct(user, product)) {
            throw new AppException(ErrorApp.REVIEW_EXISTED);
        }

        Review review = reviewMapper.toReview(request);

        review.setUser(user);
        review.setProduct(product);

        return reviewRepository.save(review);
    }

    public Review getReviewById(Integer id) {
        return reviewRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorApp.REVIEW_NOT_FOUND));
    }

    public Review getByUserAndProduct(String username, Integer productId){
        User user = userService.getByUsername(username);

        Product product = productService.getProductById(productId);

        return reviewRepository.findByUserAndProduct(user, product);
    }

    public Slice<Review> getByRatingAndProduct(
            Integer rating, Integer productId, Integer number, Integer size, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.valueOf(order.toUpperCase()), sortBy);

        Pageable pageable = PageRequest.of(number, size, sort);

        if(rating == null) {
            return reviewRepository.findByProduct(productId, pageable);
        } else {
            return reviewRepository.findByRatingAndProduct(rating, productId, pageable);
        }
    }

    public Review updateReview(Integer id, ReviewRequest request, String username) {
        Review review = getReviewById(id);

        if(!review.getUser().getUsername().equals(username)) {
            throw new AppException(ErrorApp.REVIEW_ACCESS_DENIED);
        }

        review.setComment(request.getComment());
        review.setRating(request.getRating());

        return reviewRepository.save(review);
    }

    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }


}
