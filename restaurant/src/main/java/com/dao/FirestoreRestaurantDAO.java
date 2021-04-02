package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.objects.Restaurant;
import com.objects.Result;

public class FirestoreRestaurantDAO implements RestaurantDAO {
  private CollectionReference restaurantCol;

  public FirestoreRestaurantDAO() {
    Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
    restaurantCol = firestore.collection("restaurants");
  }


  private Restaurant documentToRestaurant(DocumentSnapshot document) {
    Map<String, Object> data = document.getData();
    if (data == null) {
      System.out.println("No data in document " + document.getId());
      return null;
    }

    return new Restaurant.Builder()
        .restName((String) data.get(Restaurant.REST_NAME))
        .address((String) data.get(Restaurant.ADDRESS))
        .maxCapacity((String) data.get(Restaurant.MAX_CAPACITY))
        .imageUrl((String) data.get(Restaurant.IMAGE_URL))
        .createdBy((String) data.get(Restaurant.CREATED_BY))
        .createdById((String) data.get(Restaurant.CREATED_BY_ID))
        .contactNumber((String) data.get(Restaurant.CONTACT_NUMBER))
        .cuisine((String) data.get(Restaurant.CUISINE))
        .id(document.getId())
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
    data.put(Restaurant.CONTACT_NUMBER, rest.getContactNumber());
    data.put(Restaurant.IMAGE_URL, rest.getImageUrl());
    data.put(Restaurant.CREATED_BY, rest.getCreatedBy());
    data.put(Restaurant.CREATED_BY_ID, rest.getCreatedById());
    data.put(Restaurant.CUISINE, rest.getCuisine());
    try {
      document.set(data).get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return id;
  }

  @Override
  public Restaurant readRestaurant(String restID) {
    try {
      DocumentSnapshot document = restaurantCol.document(restID).get().get();

      return documentToRestaurant(document);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void updateRestaurant(Restaurant rest) {
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
    try {
      document.set(data).get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
  // [END bookshelf_firestore_update]

  // [START bookshelf_firestore_delete]
  @Override
  public void deleteRestaurant(String restID) {
    try {
      restaurantCol.document(restID).delete().get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
  // [END bookshelf_firestore_delete]

  // [START bookshelf_firestore_documents_to_books]
  private List<Restaurant> documentsToRestaurants(List<QueryDocumentSnapshot> documents) {
    List<Restaurant> resultRestaurants = new ArrayList<>();
    for (QueryDocumentSnapshot snapshot : documents) {
    	resultRestaurants.add(documentToRestaurant(snapshot));
    }
    return resultRestaurants;
  }

  @Override
  public Result<Restaurant> listRestaurants(String startName) {
    Query restQuery = restaurantCol.orderBy("restName").limit(10);
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

//  @Override
//  public Result<Book> listBooksByUser(String userId, String startTitle) {
//    Query booksQuery =
//        booksCollection.orderBy("title").whereEqualTo(Book.CREATED_BY_ID, userId).limit(10);
//    if (startTitle != null) {
//      booksQuery = booksQuery.startAfter(startTitle);
//    }
//    try {
//      QuerySnapshot snapshot = booksQuery.get().get();
//      List<Book> results = documentsToBooks(snapshot.getDocuments());
//      String newCursor = null;
//      if (results.size() > 0) {
//        newCursor = results.get(results.size() - 1).getTitle();
//      }
//      return new Result<>(results, newCursor);
//    } catch (InterruptedException | ExecutionException e) {
//      e.printStackTrace();
//    }
//    return new Result<>(Lists.newArrayList(), null);
//  }
}