package com.cmpe275.finalproject.domain.users;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.cmpe275.finalproject.validation.ValidEmail;

public class UserProfile {
	@Id
	public ObjectId _id;
	
	@Indexed(unique = true)
	@ValidEmail
	private String username;
	
	private String password;
	
	private boolean enabled;
	
	private String role;
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public UserProfile(ObjectId _id, String username, String password) {
		this._id = _id;
		this.username = username;
		this.password = password;
		this.enabled = false;
		this.role = "USER";
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
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
	
	
	
	
}
