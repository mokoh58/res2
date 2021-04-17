package com.dao;

import com.objects.UserAccount;

public interface UserAccountDAO {
    String createUserAccount(UserAccount userAccount);
    
    UserAccount getUserAccount(String username, String password);

	// UserAccount readUserAccount(String userAccountID);

	// void updateUserAccount(UserAccount userAccount);

	// void deleteUserAccount(String userAccountID);
}
