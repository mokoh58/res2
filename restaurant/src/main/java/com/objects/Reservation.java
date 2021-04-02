/* Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.objects;

import java.util.Date;

public class Reservation {
	private String resoName;
	private String resoContact;
	private String resoDate;
	private String resoTime;
	private String createdBy;
	private String createdById;
	private String restId;
	private String id;
	
	public static final String RESO_NAME = "resoName";
	public static final String RESO_CONTACT = "resoContact";
	public static final String RESO_DATE = "resoDate";
	public static final String RESO_TIME = "resoTime";
	public static final String CREATED_BY = "createdBy";
	public static final String CREATED_BY_ID = "createdById";
	public static final String REST_ID = "restId";
	public static final String ID = "id";


	// We use a Builder pattern here to simplify and standardize construction of
	// Book objects.
	private Reservation(Builder builder) {
		this.resoName = builder.resoName;
		this.resoContact = builder.resoContact;
		this.createdBy = builder.createdBy;
		this.createdById = builder.createdById;
		this.resoDate = builder.resoDate;
		this.resoTime = builder.resoTime;
		this.restId = builder.restId;
		this.id = builder.id;
	}

	public static class Builder {
		private String resoName;
		private String resoContact;
		private String resoDate;
		private String resoTime;
		private String createdBy;
		private String createdById;
		private String restId;
		private String id;

		public Builder resoName(String resoName) {
			this.resoName = resoName;
			return this;
		}

		public Builder resoContact(String resoContact) {
			this.resoContact = resoContact;
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

	@Override
	public String toString() {
		return "Reservation [resoName=" + resoName + ", resoContact=" + resoContact + ", resoDate=" + resoDate
				+ ", resoTime=" + resoTime + ", createdBy=" + createdBy + ", createdById=" + createdById + ", restId="
				+ restId + ", id=" + id + "]";
	}
}
