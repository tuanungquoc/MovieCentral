package com.cmpe275.finalproject.domain.users;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.cmpe275.finalproject.validation.ValidEmail;

public class VerificationToken {
	@Id
	public ObjectId _id;
	
	
	private String token;
	
	private ObjectId userId;
	
	public VerificationToken(ObjectId _id, String token, ObjectId userId) {
		this._id = _id;
		this.token = token;
		this.userId = userId;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}
}
