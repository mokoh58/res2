package com.dao;

import com.objects.Favourite;
import com.objects.Result;

public interface FavouriteDAO {
	String createFavourite(Favourite fav);

	//Favourite readFavourite(String favId);

    void deleteFavourite(String resID);
    
    Favourite hasFavourite(String restaurantId, String userId);

    //Result<Favourite> listFavouriteByUser(String userId);
    
    //Result<Favourite> listFavouriteByRestaurant(String restaurantId);
}
