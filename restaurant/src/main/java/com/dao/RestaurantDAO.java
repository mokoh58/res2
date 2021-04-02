/* Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dao;

import com.objects.Restaurant;
import com.objects.Result;

public interface RestaurantDAO {
	String createRestaurant(Restaurant res);

	Restaurant readRestaurant(String resID);

	void updateRestaurant(Restaurant res);

	void deleteRestaurant(String resID);

	Result<Restaurant> listRestaurants(String startCursor);

	// Result<Restaurant> listBooksByUser(String userId, String startCursor);
}
