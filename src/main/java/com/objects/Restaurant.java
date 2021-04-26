package com.objects;

import java.util.Date;

public class Restaurant {
	private String restName;
	private String address;
    private String maxCapacity;
    private String occupiedSeats;
	private String contactNumber;
	private String createdBy;
    private String createdById;
    private Date createDt;
	private String id;
	private String imageUrl;
    private String cuisine;
    private String operatingHours;
    private String crowdLevel;

	public static final String REST_NAME = "restName";
	public static final String ADDRESS = "address";
	public static final String CUISINE = "cuisine";
    public static final String CREATED_BY = "createdBy";
    public static final String CREATE_DT = "createDt";
	public static final String CREATED_BY_ID = "createdById";
	public static final String CONTACT_NUMBER = "contactNumber";
    public static final String MAX_CAPACITY = "maxCapacity";
    public static final String OCC_SEATS = "occupiedSeats";
	public static final String ID = "id";
    public static final String IMAGE_URL = "imageUrl";
    public static final String OPERATING_HOURS = "operatingHours";

	// We use a Builder pattern here to simplify and standardize construction of
	// Book objects.
	private Restaurant(Builder builder) {
		this.restName = builder.restName;
		this.address = builder.address;
		this.createdBy = builder.createdBy;
        this.createdById = builder.createdById;
        this.createDt = builder.createDt;
        this.maxCapacity = builder.maxCapacity;
        this.occupiedSeats = builder.occupiedSeats;
		this.contactNumber = builder.contactNumber;
		this.id = builder.id;
		this.imageUrl = builder.imageUrl;
        this.cuisine = builder.cuisine;
        this.operatingHours = builder.operatingHours;
	}

	public static class Builder {
		private String restName;
		private String address;
		private String createdBy;
        private String createdById;
        private Date createDt;
        private String maxCapacity;
        private String occupiedSeats;
		private String contactNumber;
		private String id;
		private String imageUrl;
        private String cuisine;
        private String operatingHours;

		public Builder restName(String restName) {
			this.restName = restName;
			return this;
		}

		public Builder address(String address) {
			this.address = address;
			return this;
		}

		public Builder createdBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}

		public Builder createdById(String createdById) {
			this.createdById = createdById;
			return this;
        }
        
        public Builder createDt(Date createDt){
            this.createDt = createDt;
            return this;
        }

		public Builder maxCapacity(String maxCapacity) {
			this.maxCapacity = maxCapacity;
			return this;
        }
        
        public Builder occupiedSeats(String occupiedSeats) {
			this.occupiedSeats = occupiedSeats;
			return this;
		}

		public Builder contactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
			return this;
		}

		public Builder cuisine(String cuisine) {
			this.cuisine = cuisine;
			return this;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder imageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
        }
        
        public Builder operatingHours(String operatingHours) {
            this.operatingHours = operatingHours;
            return this;
        }

		public Restaurant build() {
			return new Restaurant(this);
		}
	}

	public String getRestName() {
		return restName;
	}

	public void setRestName(String restName) {
		this.restName = restName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
    }
    
    public Date getCreatedDt() {
		return createDt;
	}

	public void setCreatedDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(String maxCapacity) {
		this.maxCapacity = maxCapacity;
    }
    
    public String getOccupiedSeats() {
		return occupiedSeats;
	}

	public void setOccupiedSeats(String occupiedSeats) {
		this.occupiedSeats = occupiedSeats;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
    }
    
    public String getOperatingHours() {
		return operatingHours;
	}

	public void setOperatingHours(String operatingHours) {
		this.operatingHours = operatingHours;
    }
    
    public String getCrowdLevel() {
		return crowdLevel;
	}

	public void setCrowdLevel(String crowdLevel) {
		this.crowdLevel = crowdLevel;
	}

	@Override
	public String toString() {
		return "Restaurant [restName=" + restName + ", address=" + address + ", maxCapacity=" + maxCapacity + ", occupiedSeats= " + occupiedSeats
				+ ", contactNumber=" + contactNumber + ", createdBy=" + createdBy + ", createdById=" + createdById
				+ ", id=" + id + ", imageUrl=" + imageUrl + "]";
	}

}
