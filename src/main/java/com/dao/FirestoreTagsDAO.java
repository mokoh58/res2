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
import com.objects.Tags;
import com.objects.Result;

public class FirestoreTagsDAO implements TagsDAO {

    private static final Logger logger = Logger.getLogger(FirestoreTagsDAO.class.getName());

	private CollectionReference tagsCol;

	public FirestoreTagsDAO() {
        logger.log(Level.INFO, "Creating FirestoreTagsDAO");
		Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
		tagsCol = firestore.collection("tags");
	}

	private Tags documentToTags(DocumentSnapshot document) {
		Map<String, Object> data = document.getData();
		if (data == null) {
			System.out.println("No data in document " + document.getId());
			return null;
        }

        return new Tags.Builder()
                .restaurantId((String) data.get(Tags.RESTAURANT_ID))
                .tag((String) data.get(Tags.TAG))
                .build();
	}

	@Override
	public String createTags(Tags tag) {
		String id = UUID.randomUUID().toString();
		DocumentReference document = tagsCol.document(id);
		Map<String, Object> data = Maps.newHashMap();

		data.put(Tags.RESTAURANT_ID, tag.getRestaurantId());
		data.put(Tags.TAG, tag.getTag());
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

        //logger.log(Level.INFO, "Created Restaurant with id: " + id);

		return id;
    }

	@Override
	public void deleteTags(String resId) {
		try {
            Query query = tagsCol.whereEqualTo("restaurantId", resId);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                tagsCol.document(document.getId()).delete();
            }
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private List<Tags> documentsToTags(List<QueryDocumentSnapshot> documents) {
		List<Tags> result = new ArrayList<>();
		for (QueryDocumentSnapshot snapshot : documents) {
			result.add(documentToTags(snapshot));
		}
		return result;
    }


    @Override
    public ArrayList<Tags> getTags(String resId){
        ArrayList<Tags> results = new ArrayList<Tags>();
        Query query = tagsCol.whereEqualTo("restaurantId", resId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                Tags tag = documentToTags(document);
                results.add(tag);
            }
        } catch (Exception e){
            logger.log(Level.INFO, "Exception caught in FirestoreTagsDAO >  getTags()");
        }
        
        return results;
    }

}
