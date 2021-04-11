package main.java.com.dao;

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
import main.java.com.objects.UserAccount;

public class FirestoreUserAccountDAO implements UserAccountDAO {

    private static final Logger logger = Logger.getLogger(FirestoreUserAccountDAO.class.getName());

	private CollectionReference userAccountCol;

    public FirestoreUserAccountDAO() {
        logger.log(Level.INFO, "Creating FirestoreUserAccountDAO");
		Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
		userAccountCol = firestore.collection("userAccounts");
    }
    
    private UserAccount documentToUserAccount(DocumentSnapshot document) {
		Map<String, Object> data = document.getData();
		if (data == null) {
			System.out.println("No data in document " + document.getId());
			return null;
        }
        
        logger.log(Level.INFO, "documentToUserAccount Document: " + data.toString());

        return new UserAccount.Builder()
                .username((String) data.get(UserAccount.USERNAME))
                .password((String) data.get(UserAccount.PASSWORD))
                .accountType((String) data.get(UserAccount.USERNAME))
                .firstName((String) data.get(UserAccount.FIRST_NAME))
                .lastName((String) data.get(UserAccount.LAST_NAME))
                .email((String) data.get(UserAccount.EMAIL))
                .contactNumber((String) data.get(UserAccount.CONTACT_NUMBER))
                .userAccountId(document.getId())
                .build();
    }
    
    @Override
	public String createUserAccount(main.java.com.objects.UserAccount userAccount) {
		String id = UUID.randomUUID().toString();
		DocumentReference document = userAccountCol.document(id);
		Map<String, Object> data = Maps.newHashMap();

		data.put(UserAccount.USERNAME, userAccount.getUsername());
		data.put(UserAccount.PASSWORD, userAccount.getPassword());
        data.put(UserAccount.ACCOUNT_TYPE, userAccount.getAccountType());
        data.put(UserAccount.EMAIL, userAccount.getEmail());
		data.put(UserAccount.FIRST_NAME, userAccount.getFirstName());
		data.put(UserAccount.LAST_NAME, userAccount.getLastName());
        data.put(UserAccount.CONTACT_NUMBER, userAccount.getContactNumber());
        data.put(UserAccount.USER_ACCOUNT_ID, id);
		try {
			document.set(data).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

        logger.log(Level.INFO, "Created UserAccount with id: " + id);

		return id;
	}

}