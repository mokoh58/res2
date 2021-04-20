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
import com.objects.Favourite;
import com.objects.Result;

public class FirestoreFavouriteDAO implements FavouriteDAO {

    private static final Logger logger = Logger.getLogger(FirestoreFavouriteDAO.class.getName());

	private CollectionReference favouriteCol;

	public FirestoreFavouriteDAO() {
        logger.log(Level.INFO, "Creating FirestoreFavouriteDAO");
		Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
		favouriteCol = firestore.collection("favourite");
	}

	private Favourite documentToFavourite(DocumentSnapshot document) {
		Map<String, Object> data = document.getData();
		if (data == null) {
			System.out.println("No data in document " + document.getId());
			return null;
        }

        return new Favourite.Builder()
                .restaurantId((String) data.get(Favourite.RESTAURANT_ID))
                .userId((String) data.get(Favourite.USER_ID))
                .id(document.getId())
                .build();
	}

	@Override
	public String createFavourite(Favourite favourite) {
		String id = UUID.randomUUID().toString();
		DocumentReference document = favouriteCol.document(id);
		Map<String, Object> data = Maps.newHashMap();

		data.put(Favourite.RESTAURANT_ID, favourite.getRestaurantId());
		data.put(Favourite.USER_ID, favourite.getUserId());
        data.put(Favourite.ID, id);
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

        //logger.log(Level.INFO, "Created Restaurant with id: " + id);

		return id;
	}

	@Override
	public void deleteFavourite(String favId) {
		try {
			favouriteCol.document(favId).delete().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private List<Favourite> documentsToFavourite(List<QueryDocumentSnapshot> documents) {
		List<Favourite> resultFavourite = new ArrayList<>();
		for (QueryDocumentSnapshot snapshot : documents) {
			resultFavourite.add(documentToFavourite(snapshot));
		}
		return resultFavourite;
    }
    
    public Favourite hasFavourite(String restaurantId, String userId){

        Query query = favouriteCol.whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                Favourite favourite = documentToFavourite(document);
                if (restaurantId.equals(favourite.getRestaurantId())){
                    return favourite;
                }
            }
        } catch (Exception e){
            logger.log(Level.INFO, "Exception caught in FirestoreFavouriteDAO >  hasFavourite()");
        }

        return null;
    }


    @Override
    public ArrayList<String> listFavouriteByUser(String userId){
        ArrayList<String> results = new ArrayList<String>();
        Query query = favouriteCol.whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                Favourite favourite = documentToFavourite(document);
                results.add(favourite.getRestaurantId());
            }
        } catch (Exception e){
            logger.log(Level.INFO, "Exception caught in FirestoreFavouriteDAO >  listFavouriteByUser()");
        }
        
        return results;
    }

}
