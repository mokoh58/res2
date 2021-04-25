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
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.objects.Restaurant;
import com.objects.Review;
import com.objects.Result;

public class FirestoreReviewDAO implements ReviewDAO {

    private static final Logger logger = Logger.getLogger(FirestoreReviewDAO.class.getName());

	private CollectionReference reviewCol;

	public FirestoreReviewDAO() {
        logger.log(Level.INFO, "Creating FirestoreReviewDAO");
		Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
		reviewCol = firestore.collection("review");
	}

	private Review documentToReview(DocumentSnapshot document) {
		Map<String, Object> data = document.getData();
		if (data == null) {
			System.out.println("No data in document " + document.getId());
			return null;
        }

        return new Review.Builder()
                .restaurantId((String) data.get(Review.RESTAURANT_ID))
                .userId((String) data.get(Review.USER_ID))
                .username((String) data.get(Review.USERNAME))
                .createDt((String) data.get(Review.CREATE_DT))
                .dateOfVisit((String) data.get(Review.DATE_OF_VISIT))
                .remarks((String) data.get(Review.REMARKS))
                .rating((String) data.get(Review.RATING))
                .id(document.getId())
                .build();
	}

	@Override
	public String createReview(Review review) {
		String id = UUID.randomUUID().toString();
		DocumentReference document = reviewCol.document(id);
		Map<String, Object> data = Maps.newHashMap();

		data.put(Review.RESTAURANT_ID, review.getRestaurantId());
        data.put(Review.USER_ID, review.getUserId());
        data.put(Review.USERNAME, review.getUsername());
        data.put(Review.CREATE_DT, review.getCreateDt());
        data.put(Review.DATE_OF_VISIT, review.getDateOfVisit());
        data.put(Review.REMARKS, review.getRemarks());
        data.put(Review.RATING, review.getRating());
        data.put(Review.ID, id);
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

        //logger.log(Level.INFO, "Created Restaurant with id: " + id);

		return id;
	}

	@Override
	public void deleteReview(String reviewId) {
		try {
			reviewCol.document(reviewId).delete().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private List<Review> documentsToReview(List<QueryDocumentSnapshot> documents) {
		List<Review> resultReview = new ArrayList<>();
		for (QueryDocumentSnapshot snapshot : documents) {
			resultReview.add(documentToReview(snapshot));
		}
		return resultReview;
    }


    @Override
    public List<Review> getReviewsByRestaurant(String restaurantId){
        List<Review> results = new ArrayList<Review>();
        Query query = reviewCol.whereEqualTo("restaurantId", restaurantId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                Review review = documentToReview(document);
                results.add(review);
            }
        } catch (Exception e){
            logger.log(Level.INFO, "Exception caught in FirestoreReviewDAO >  getReviewsByRestaurant()");
        }
        
        return results;
    }

}
