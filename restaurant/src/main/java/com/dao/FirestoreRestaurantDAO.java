package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.objects.Restaurant;
import com.objects.Result;

public class FirestoreRestaurantDAO implements RestaurantDAO {

    private static final Logger logger = Logger.getLogger(FirestoreRestaurantDAO.class.getName());

	private CollectionReference restaurantCol;

	public FirestoreRestaurantDAO() {
        logger.log(Level.INFO, "Creating FirestoreRestaurantDAO");
		Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
		restaurantCol = firestore.collection("restaurants");
	}

	private Restaurant documentToRestaurant(DocumentSnapshot document) {
		Map<String, Object> data = document.getData();
		if (data == null) {
			System.out.println("No data in document " + document.getId());
			return null;
        }
        
        //logger.log(Level.INFO, "documentToRestaurant Document: " + data.toString());

        return new Restaurant.Builder()
                .restName((String) data.get(Restaurant.REST_NAME))
                .address((String) data.get(Restaurant.ADDRESS))
                .maxCapacity((String) data.get(Restaurant.MAX_CAPACITY))
                .occupiedSeats((String) data.get(Restaurant.OCC_SEATS))
                .imageUrl((String) data.get(Restaurant.IMAGE_URL))
                .createdBy((String) data.get(Restaurant.CREATED_BY))
				.createdById((String) data.get(Restaurant.CREATED_BY_ID))
				.contactNumber((String) data.get(Restaurant.CONTACT_NUMBER))
                .cuisine((String) data.get(Restaurant.CUISINE))
                .id(document.getId())
                .operatingHours((String) data.get(Restaurant.OPERATING_HOURS))
                .build();
	}

	@Override
	public String createRestaurant(Restaurant rest) {
		String id = UUID.randomUUID().toString();
		DocumentReference document = restaurantCol.document(id);
		Map<String, Object> data = Maps.newHashMap();

		data.put(Restaurant.REST_NAME, rest.getRestName());
		data.put(Restaurant.ADDRESS, rest.getAddress());
        data.put(Restaurant.MAX_CAPACITY, rest.getMaxCapacity());
        data.put(Restaurant.OCC_SEATS, rest.getOccupiedSeats());
		data.put(Restaurant.CONTACT_NUMBER, rest.getContactNumber());
		data.put(Restaurant.IMAGE_URL, rest.getImageUrl());
		data.put(Restaurant.CREATED_BY, rest.getCreatedBy());
		data.put(Restaurant.CREATED_BY_ID, rest.getCreatedById());
        data.put(Restaurant.CUISINE, rest.getCuisine());
        data.put(Restaurant.OPERATING_HOURS, rest.getOperatingHours());
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

        //logger.log(Level.INFO, "Created Restaurant with id: " + id);

		return id;
	}

	@Override
	public Restaurant readRestaurant(String restID) {
		try {
             logger.log(Level.INFO, "readRestaurant id: " + restID);

			DocumentSnapshot document = restaurantCol.document(restID).get().get();

			return documentToRestaurant(document);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateRestaurant(Restaurant rest) {

         logger.log(Level.INFO, "In updateRestaurant");

		DocumentReference document = restaurantCol.document(rest.getId());
		Map<String, Object> data = Maps.newHashMap();

		data.put(Restaurant.REST_NAME, rest.getRestName());
		data.put(Restaurant.ADDRESS, rest.getAddress());
		data.put(Restaurant.MAX_CAPACITY, rest.getMaxCapacity());
		data.put(Restaurant.CONTACT_NUMBER, rest.getContactNumber());
		data.put(Restaurant.IMAGE_URL, rest.getImageUrl());
		data.put(Restaurant.CREATED_BY, rest.getCreatedBy());
		data.put(Restaurant.CREATED_BY_ID, rest.getCreatedById());
        data.put(Restaurant.CUISINE, rest.getCuisine());
        data.put(Restaurant.OPERATING_HOURS, rest.getOperatingHours());
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteRestaurant(String restID) {
		try {
			restaurantCol.document(restID).delete().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private List<Restaurant> documentsToRestaurants(List<QueryDocumentSnapshot> documents) {
		List<Restaurant> resultRestaurants = new ArrayList<>();
		for (QueryDocumentSnapshot snapshot : documents) {
			resultRestaurants.add(documentToRestaurant(snapshot));
		}
		return resultRestaurants;
	}

	@Override
	public Result<Restaurant> listRestaurants(String startName) {
        logger.log(Level.INFO, "In listRestaurants");

        Query restQuery = restaurantCol.orderBy("restName").limit(10);
        //Query restQuery = restaurantCol.orderBy("createDt", Direction.DESCENDING).limit(10);
		if (startName != null) {
			restQuery = restQuery.startAfter(startName);
		}
		try {
			QuerySnapshot snapshot = restQuery.get().get();
			List<Restaurant> results = documentsToRestaurants(snapshot.getDocuments());
			String newCursor = null;
			if (results.size() > 0) {
				newCursor = results.get(results.size() - 1).getRestName();
			}
			return new Result<>(results, newCursor);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return new Result<>(Lists.newArrayList(), null);
	}

    @Override
    public void UpdateOccupiedSeats(String restId, Integer numSeats) {
        logger.log(Level.INFO, "UpdateOccupiedSeats restId " + restId);

        try {
            DocumentReference docRef = restaurantCol.document(restId);

            ApiFuture<WriteResult> future = docRef.update("occupiedSeats", numSeats.toString());

            WriteResult result = future.get();

            System.out.println("Write result: " + result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
