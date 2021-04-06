package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
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
import com.objects.Reservation;
import com.objects.Reservation;
import com.objects.Result;

public class FirestoreReservationDAO implements ReservationDAO {

    private static final Logger logger = Logger.getLogger(FirestoreReservationDAO.class.getName());

	private final CollectionReference resoCol;

	public FirestoreReservationDAO() {
        logger.log(Level.INFO, "Creating FirestoreReservationDAO");
		final Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
		resoCol = firestore.collection("reservations");
	}

	private Reservation documentToReservation(final DocumentSnapshot document) {
		final Map<String, Object> data = document.getData();
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
                .numPax((String) data.get(Reservation.NUM_PAX))
                .build();
	}

	@Override
	public String createReservation(final Reservation reso) {
		final String id = UUID.randomUUID().toString();
		final DocumentReference document = resoCol.document(id);
        final Map<String, Object> data = Maps.newHashMap();
        Timestamp resoTS = null;
        try {

            final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            final Date resoDate = sdf.parse(reso.getResoDate() + " " + reso.getResoTime());

            logger.log(Level.INFO, "Date is " + resoDate.toString());

            resoTS = Timestamp.of(resoDate);        
        } catch (final Exception e) {
            e.printStackTrace();
        }
		data.put(Reservation.RESO_NAME, reso.getResoName());
		data.put(Reservation.RESO_CONTACT, reso.getResoContact());
        data.put(Reservation.RESO_DATE, reso.getResoDate());
        data.put(Reservation.RESO_TIME, reso.getResoTime());
        data.put(Reservation.RESO_DATE, reso.getResoDate());
        data.put(Reservation.RESO_TS, resoTS);
		data.put(Reservation.REST_ID, reso.getRestId());
		data.put(Reservation.CREATED_BY, reso.getCreatedBy());
        data.put(Reservation.CREATED_BY_ID, reso.getCreatedById());
        data.put(Reservation.NUM_PAX, reso.getNumPax());
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

        logger.log(Level.INFO, "Created Reservation with id: " + id);

		return id;
	}

	@Override
	public Reservation readReservation(final String resoId) {
		try {
             logger.log(Level.INFO, "readReservation id: " + resoId);

			final DocumentSnapshot document = resoCol.document(resoId).get().get();

			return documentToReservation(document);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateReservation(final Reservation reso) {

        logger.log(Level.INFO, "In updateReservation");

		final DocumentReference document = resoCol.document(reso.getId());
		final Map<String, Object> data = Maps.newHashMap();

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
	public void deleteReservation(final String resoId) {
		try {
			resoCol.document(resoId).delete().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private List<Reservation> documentsToReservations(final List<QueryDocumentSnapshot> documents) {
		final List<Reservation> resultReservations = new ArrayList<>();
		for (final QueryDocumentSnapshot snapshot : documents) {
			resultReservations.add(documentToReservation(snapshot));
		}
		return resultReservations;
	}

	@Override
	public Result<Reservation> listReservations(final String startName) {
        logger.log(Level.INFO, "In listReservations");

		Query restQuery = resoCol.orderBy("restId");
		if (startName != null) {
			restQuery = restQuery.startAfter(startName);
		}
		try {
			final QuerySnapshot snapshot = restQuery.get().get();
			final List<Reservation> results = documentsToReservations(snapshot.getDocuments());
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
    
    @Override
    public Result<Reservation> listReservationsByRestaurant(final String restId ,final String startName) {
        logger.log(Level.INFO, "In listReservations by " + restId);

		Query resoQuery = resoCol.orderBy("resoTimeStamp", Query.Direction.ASCENDING).whereEqualTo(Reservation.REST_ID, restId);
		if (startName != null) {
			resoQuery = resoQuery.startAfter(startName);
		}
		try {
            final QuerySnapshot snapshot = resoQuery.get().get();

            //logger.log(Level.INFO, "Size: " + snapshot.getDocuments().size());

			final List<Reservation> results = documentsToReservations(snapshot.getDocuments());
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
    @Override
    public List<Reservation> getReservationsByRestaurant(final String restId) {
        final ApiFuture<QuerySnapshot> resos = resoCol.whereEqualTo(Reservation.REST_ID, restId).get();
        try {
            final List<QueryDocumentSnapshot> documents = resos.get().getDocuments();

            return documentsToReservations(documents);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
