package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.Timestamp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.objects.OperatingHoursCode;
import com.objects.Result;

public class FirestoreOperatingHoursDAO implements OperatingHoursDAO {

    private static final Logger logger = Logger.getLogger(FirestoreOperatingHoursDAO.class.getName());

	private final CollectionReference ohCol;

	public FirestoreOperatingHoursDAO() {
        logger.log(Level.INFO, "Creating FirestoreOperatingHoursDAO");
		final Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
		ohCol = firestore.collection("OperatingHours");
	}

	@Override
	public List<OperatingHoursCode> listOperatingHours() {
        List<OperatingHoursCode> results = null;
		Query restQuery = ohCol.orderBy("code");
		try {
			QuerySnapshot snapshot = restQuery.get().get();
			results = documentsToOH(snapshot.getDocuments());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return results;
    }

    private OperatingHoursCode documentToOH(final DocumentSnapshot document) {
		final Map<String, Object> data = document.getData();
		if (data == null) {
			System.out.println("No data in document " + document.getId());
			return null;
        }
        
        OperatingHoursCode ohItem = new OperatingHoursCode();

        ohItem.setCode(data.get("code").toString());
        ohItem.setValue(data.get("value").toString());

        return ohItem;
    }
    
    private List<OperatingHoursCode> documentsToOH(final List<QueryDocumentSnapshot> documents) {
		final List<OperatingHoursCode> resultOH = new ArrayList<>();
		for (final QueryDocumentSnapshot snapshot : documents) {
			resultOH.add(documentToOH(snapshot));
		}
		return resultOH;
	}
}
