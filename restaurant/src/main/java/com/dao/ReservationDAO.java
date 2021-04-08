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

import java.util.List;

import com.objects.Reservation;
import com.objects.Result;

public interface ReservationDAO {
	String createReservation(Reservation res);

	Reservation readReservation(String resID);

	void updateReservation(Reservation res);

	void deleteReservation(String resID);

	Result<Reservation> listReservations(String startCursor);

    Result<Reservation> listReservationsByRestaurant(String restId, String startCursor);

    List<Reservation> getReservationsByRestaurant(String restId);
}
