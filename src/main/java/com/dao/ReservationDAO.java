package com.dao;

import java.util.List;

import com.objects.Reservation;
import com.objects.Result;

public interface ReservationDAO {
	String createReservation(Reservation res, String userAccountId);

	Reservation readReservation(String resID);

	void updateReservation(Reservation res);

	void deleteReservation(String resID);

	Result<Reservation> listReservations(String startCursor);

    Result<Reservation> listReservationsByRestaurant(String restId, String startCursor);

    List<Reservation> getReservationsByRestaurant(String restId);
}
