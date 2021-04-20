package com.objects;

public class UserAccount {
    private String userAccountId;
    private String username;
    private String password;
    private String accountType;
    private String email;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String favourites;
    public static final String USER_ACCOUNT_ID = "userAccountId";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String CONTACT_NUMBER = "contactNumber";
    public static final String FAVOURITES = "favourites";

    private UserAccount(Builder builder) {
        this.userAccountId = builder.userAccountId;
        this.username = builder.username;
        this.password = builder.password;
        this.accountType = builder.accountType;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.contactNumber = builder.contactNumber;
        this.favourites = builder.favourites;
    }

    public static class Builder {
        private String userAccountId;
        private String username;
        private String password;
        private String accountType;
        private String email;
        private String firstName;
        private String lastName;
        private String contactNumber;
        private String favourites;

        public Builder userAccountId(String userAccountId) {
			this.userAccountId = userAccountId;
			return this;
        }

        public Builder username(String username) {
			this.username = username;
			return this;
        }
        
        public Builder password(String password) {
			this.password = password;
			return this;
        }
        
        public Builder accountType(String accountType) {
			this.accountType = accountType;
			return this;
        }
        
        public Builder email(String email) {
			this.email = email;
			return this;
        }
        
        public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
        }
        
        public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
        }
        
        public Builder contactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
			return this;
        }

        public Builder favourites(String favourites) {
            this.favourites = favourites;
            return this;
        }
        
        public UserAccount build() {
			return new UserAccount(this);
		}
    }

    public String getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
    }
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
    }
    
    public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
    }
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
    }
    
    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
    }
    
    public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
    }
    
    public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
    }
    
    public String getFavourites() {
        return favourites;
    }

    public void setFavourites (String favourites){
        this.favourites = favourites;
    }

    public boolean isContain(String[] favouritesArray, String restaurantId){
        for (int i = 0; i < favouritesArray.length; i++){
            if (favouritesArray[i].equals(restaurantId)){
                return true;
            }
        }
        return false;
    }

    public boolean isFavourite(String restaurantId){
        String[] favouritesArray = favourites.split(",");
        if(isContain(favouritesArray,restaurantId)){
            return true;
        }
        return false;
    }

    public void removeFavourite(String restaurantId){

    }

    public void addFavourite(String restaurantId){
        favourites = favourites + "," + restaurantId;
    }

}