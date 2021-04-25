package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.objects.Review;
import com.objects.Result;

public interface ReviewDAO {
	String createReview(Review review);

    void deleteReview(String reviewId);

    List<Review> getReviewsByRestaurant(String restaurantId);
}
