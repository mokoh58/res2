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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.objects.Reservation;
import com.objects.Reservation;
import com.objects.Result;

public class FirestoreReservationDAO implements ReservationDAO {

    private static final Logger logger = Logger.getLogger(FirestoreReservationDAO.class.getName());

	private CollectionReference resoCol;

	public FirestoreReservationDAO() {
        logger.log(Level.INFO, "Creating FirestoreReservationDAO");
		Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
		resoCol = firestore.collection("reservations");
	}

	private Reservation documentToReservation(DocumentSnapshot document) {
		Map<String, Object> data = document.getData();
		if (data == null) {
			System.out.println("No data in document " + document.getId());
			return null;
        }
        
        logger.log(Level.INFO, "documentToReservation Document: " + data.toString());

        return new Reservation.Builder()
                .resoName((String) data.get(Reservation.RESO_NAME))
                .resoContact((String) data.get(Reservation.RESO_CONTACT))
                .resoDate((String) data.get(Reservation.RESO_DATE))
                .resoTime((String) data.get(Reservation.RESO_TIME))
                .createdBy((String) data.get(Reservation.CREATED_BY))
				.createdById((String) data.get(Reservation.CREATED_BY_ID))
                .restId((String) data.get(Reservation.REST_ID))
                .id(document.getId())
                .build();
	}

	@Override
	public String createReservation(Reservation reso) {
		String id = UUID.randomUUID().toString();
		DocumentReference document = resoCol.document(id);
		Map<String, Object> data = Maps.newHashMap();

		data.put(Reservation.RESO_NAME, reso.getResoName());
		data.put(Reservation.RESO_CONTACT, reso.getResoContact());
        data.put(Reservation.RESO_DATE, reso.getResoDate());
        data.put(Reservation.RESO_TIME, reso.getResoTime());
		data.put(Reservation.REST_ID, reso.getRestId());
		data.put(Reservation.CREATED_BY, reso.getCreatedBy());
		data.put(Reservation.CREATED_BY_ID, reso.getCreatedById());
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

        logger.log(Level.INFO, "Created Reservation with id: " + id);

		return id;
	}

	@Override
	public Reservation readReservation(String resoId) {
		try {
             logger.log(Level.INFO, "readReservation id: " + resoId);

			DocumentSnapshot document = resoCol.document(resoId).get().get();

			return documentToReservation(document);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateReservation(Reservation reso) {

        logger.log(Level.INFO, "In updateReservation");

		DocumentReference document = resoCol.document(reso.getId());
		Map<String, Object> data = Maps.newHashMap();

		data.put(Reservation.RESO_NAME, reso.getResoName());
		data.put(Reservation.RESO_CONTACT, reso.getResoContact());
		data.put(Reservation.RESO_DATE, reso.getResoDate());
		data.put(Reservation.RESO_TIME, reso.getResoTime());
		data.put(Reservation.CREATED_BY, reso.getCreatedBy());
		data.put(Reservation.CREATED_BY_ID, reso.getCreatedById());
		data.put(Reservation.REST_ID, reso.getRestId());
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteReservation(String resoId) {
		try {
			resoCol.document(resoId).delete().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private List<Reservation> documentsToReservations(List<QueryDocumentSnapshot> documents) {
		List<Reservation> resultReservations = new ArrayList<>();
		for (QueryDocumentSnapshot snapshot : documents) {
			resultReservations.add(documentToReservation(snapshot));
		}
		return resultReservations;
	}

	// @Override
	public Result<Reservation> listReservations(String startName) {
        logger.log(Level.INFO, "In listReservations");

		Query restQuery = resoCol.orderBy("restId");
		if (startName != null) {
			restQuery = restQuery.startAfter(startName);
		}
		try {
			QuerySnapshot snapshot = restQuery.get().get();
			List<Reservation> results = documentsToReservations(snapshot.getDocuments());
			String newCursor = null;
			if (results.size() > 0) {
				newCursor = results.get(results.size() - 1).getRestId();
			}
			return new Result<>(results, newCursor);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return new Result<>(Lists.newArrayList(), null);
    }
    
    public Result<Reservation> listReservationsByRestaurant(String restId ,String startName) {
        logger.log(Level.INFO, "In listReservations");

		Query resoQuery = resoCol.orderBy("restId").whereEqualTo(Reservation.REST_ID, restId);
		if (startName != null) {
			resoQuery = resoQuery.startAfter(startName);
		}
		try {
			QuerySnapshot snapshot = resoQuery.get().get();
			List<Reservation> results = documentsToReservations(snapshot.getDocuments());
			String newCursor = null;
			if (results.size() > 0) {
				newCursor = results.get(results.size() - 1).getRestId();
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
