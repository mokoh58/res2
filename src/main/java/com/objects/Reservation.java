package com.objects;

import com.google.cloud.Timestamp;

public class Reservation {
	private String resoName;
    private String resoContact;
    private String numPax;
	private String resoDate;
	private String resoTime;
	private String createdBy;
	private String createdById;
	private String restId;
    private String id;
    private Timestamp resoTimeStamp;
    private Timestamp resoTimeStampEnd;
    private String userAccountId;
    private Boolean isActive;
    private String resoEnded;
	
	public static final String RESO_NAME = "resoName";
    public static final String RESO_CONTACT = "resoContact";
    public static final String NUM_PAX = "numPax";
	public static final String RESO_DATE = "resoDate";
	public static final String RESO_TIME = "resoTime";
	public static final String CREATED_BY = "createdBy";
	public static final String CREATED_BY_ID = "createdById";
	public static final String REST_ID = "restId";
    public static final String ID = "id";
    public static final String RESO_TS = "resoTimeStamp";
    public static final String RESO_TS_END = "resoTimeStampEnd";
    public static final String USER_ACC_ID = "userAccountId";
    public static final String RESO_ENDED = "resoEnded";

	private Reservation(Builder builder) {
		this.resoName = builder.resoName;
        this.resoContact = builder.resoContact;
        this.numPax = builder.numPax;
		this.createdBy = builder.createdBy;
		this.createdById = builder.createdById;
		this.resoDate = builder.resoDate;
		this.resoTime = builder.resoTime;
        this.restId = builder.restId;
        this.userAccountId = builder.userAccountId;
        this.resoEnded = builder.resoEnded;
		this.id = builder.id;
	}

	public static class Builder {
		private String resoName;
        private String resoContact;
        private String numPax;
		private String resoDate;
        private String resoTime;
		private String createdBy;
		private String createdById;
		private String restId;
        private String id;
        private String userAccountId;
        private String resoEnded;

		public Builder resoName(String resoName) {
			this.resoName = resoName;
			return this;
		}

		public Builder resoContact(String resoContact) {
			this.resoContact = resoContact;
			return this;
        }
        
        public Builder numPax(String numPax) {
			this.numPax = numPax;
			return this;
		}
		
		public Builder resoDate(String resoDate) {
			this.resoDate = resoDate;
			return this;
		}

		public Builder resoTime(String resoTime) {
			this.resoTime = resoTime;
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

		public Builder restId(String restId) {
			this.restId = restId;
			return this;
		}
		
		public Builder id(String id) {
			this.id = id;
			return this;
        }
        
        public Builder userAccountId(String userAccountId) {
			this.userAccountId = userAccountId;
			return this;
        }
        
        public Builder resoEnded(String resoEnded) {
			this.resoEnded = resoEnded;
			return this;
		}

		public Reservation build() {
			return new Reservation(this);
		}
	}
	
	public String getResoName() {
		return resoName;
	}

	public void setResoName(String resoName) {
		this.resoName = resoName;
	}

	public String getResoContact() {
		return resoContact;
	}

	public void setResoContact(String resoContact) {
		this.resoContact = resoContact;
    }
    
    public String getNumPax() {
		return numPax;
	}

	public void setNumPax(String numPax) {
		this.numPax = numPax;
	}

	public String getResoDate() {
		return resoDate;
	}

	public void setResoDate(String resoDate) {
		this.resoDate = resoDate;
	}

	public String getResoTime() {
		return resoTime;
	}

	public void setResoTime(String resoTime) {
		this.resoTime = resoTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getRestId() {
		return restId;
	}

	public void setRestId(String restId) {
		this.restId = restId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
    }
    
    public Timestamp getResoTimeStamp() {
        return resoTimeStamp;
    }

    public void setResoTimeStamp(Timestamp resoTimeStamp) {
        this.resoTimeStamp = resoTimeStamp;
    }

    public Timestamp getResoTimeStampEnd() {
        return resoTimeStampEnd;
    }

    public void setResoTimeStampEnd(Timestamp resoTimeStampEnd) {
        this.resoTimeStampEnd = resoTimeStampEnd;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getResoEnded() {
        return resoEnded;
    }

    public void setResoEnded(String resoEnded) {
        this.resoEnded = resoEnded;
    }

	@Override
	public String toString() {
		return "Reservation [resoName=" + resoName + ", resoContact=" + resoContact + ", numPax= " + numPax + ", resoDate=" + resoDate
				+ ", resoTime=" + resoTime + ", createdBy=" + createdBy + ", createdById=" + createdById + ", restId="
				+ restId + ", id=" + id + ", userAccountId=" + userAccountId + "]";
	}
}
