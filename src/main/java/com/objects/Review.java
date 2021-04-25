package com.objects;

import java.util.Date;

public class Review {
    private String id;
    private String restaurantId;
    private String userId;
    private String createDt;
    private String dateOfVisit;
    private String remarks;
    private String rating;
	public static final String RESTAURANT_ID = "restaurantId";
	public static final String USER_ID = "userId";
    public static final String ID = "id";
    public static final String CREATE_DT = "createDt";
    public static final String DATE_OF_VISIT = "dateOfVisit";
    public static final String REMARKS = "remarks";
    public static final String RATING = "rating";

	// We use a Builder pattern here to simplify and standardize construction of
	// Book objects.
	private Review(Builder builder) {
		this.restaurantId = builder.restaurantId;
        this.userId = builder.userId;
        this.createDt = builder.createDt;
        this.dateOfVisit = builder.dateOfVisit;
        this.remarks = builder.remarks;
        this.rating = builder.rating;
		this.id = builder.id;
	}

	public static class Builder {
		private String restaurantId;
		private String userId;
        private String id;
        private String createDt;
        private String dateOfVisit;
        private String remarks;
        private String rating;

		public Builder restaurantId(String restaurantId) {
			this.restaurantId = restaurantId;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
        }
        
        public Builder createDt(String createDt){
            this.createDt = createDt;
            return this;
        }

        public Builder dateOfVisit(String dateOfVisit){
            this.dateOfVisit = dateOfVisit;
            return this;
        }

        public Builder remarks(String remarks){
            this.remarks = remarks;
            return this;
        }

        public Builder rating(String rating){
            this.rating = rating;
            return this;
        }

		public Review build() {
			return new Review(this);
		}
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
    }
    
    public String getCreateDt() {
		return createDt;
	}

	public void setCreateDt(String createDt) {
		this.createDt = createDt;
    }
    
    public String getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(String dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
    }
    
    public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
    }
    
    public String getRating() {
        return rating;
    }

    public void setRating (String rating){
        this.rating = rating;
    }

	@Override
	public String toString() {
		return "Favourite [restaurantId=" + restaurantId + ", userId=" + userId + ", id=" + id + ", createDt=" + createDt + "]";
	}

}
