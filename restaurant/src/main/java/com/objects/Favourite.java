package com.objects;

import java.util.Date;

public class Favourite {
    private String restaurantId;
    private String userId;
    private String id;
	public static final String RESTAURANT_ID = "restaurantId";
	public static final String USER_ID = "userId";
	public static final String ID = "id";

	// We use a Builder pattern here to simplify and standardize construction of
	// Book objects.
	private Favourite(Builder builder) {
		this.restaurantId = builder.restaurantId;
		this.userId = builder.userId;
		this.id = builder.id;
	}

	public static class Builder {
		private String restaurantId;
		private String userId;
		private String id;

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

		public Favourite build() {
			return new Favourite(this);
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

	@Override
	public String toString() {
		return "Favourite [restaurantId=" + restaurantId + ", userId=" + userId + ", id=" + id + "]";
	}

}
