package com.objects;

public class Tags {
    private String restaurantId;
    private String tag;
	public static final String RESTAURANT_ID = "restaurantId";
	public static final String TAG = "tag";

	// We use a Builder pattern here to simplify and standardize construction of
	// Book objects.
	private Tags(Builder builder) {
		this.restaurantId = builder.restaurantId;
		this.tag = builder.tag;
	}

	public static class Builder {
		private String restaurantId;
		private String tag;

		public Builder restaurantId(String restaurantId) {
			this.restaurantId = restaurantId;
			return this;
		}

		public Builder tag(String tag) {
			this.tag = tag;
			return this;
		}

		public Tags build() {
			return new Tags(this);
		}
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Tags [restaurantId=" + restaurantId + ", tag=" + tag + "]";
	}

}
