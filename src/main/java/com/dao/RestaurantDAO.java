package com.dao;

import com.objects.Restaurant;
import com.objects.Result;

public interface RestaurantDAO {
	String createRestaurant(Restaurant res);

	Restaurant readRestaurant(String resID);

	void updateRestaurant(Restaurant res);

	void deleteRestaurant(String resID);

	Result<Restaurant> listRestaurants(String startCursor);

	void UpdateOccupiedSeats(String restID,Integer numSeats);
}
