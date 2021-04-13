package main.java.com.dao;

import main.java.com.objects.UserAccount;

public interface UserAccountDAO {
    String createUserAccount(UserAccount userAccount);
    
    UserAccount getUserAccount(String username, String password);

	// UserAccount readUserAccount(String userAccountID);

	// void updateUserAccount(UserAccount userAccount);

	// void deleteUserAccount(String userAccountID);
}
