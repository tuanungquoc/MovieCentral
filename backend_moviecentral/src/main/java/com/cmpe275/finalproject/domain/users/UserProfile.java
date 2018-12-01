package com.cmpe275.finalproject.domain.users;

import java.time.LocalDateTime;
import java.util.Date;

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
	
	private String profileName;
	

	private boolean enabled;
	
	private String role;
	
	private boolean isSubcribed;
	
	private LocalDateTime nextRenewalDate;
	
	public boolean isSubcribed() {
		return isSubcribed;
	}

	public void setSubcribed(boolean isSubcribed) {
		this.isSubcribed = isSubcribed;
	}

	public LocalDateTime getNextRenewalDate() {
		return nextRenewalDate;
	}

	public void setNextRenewalDate(LocalDateTime nextRenewalDate) {
		this.nextRenewalDate = nextRenewalDate;
	}

	
	public UserProfile(ObjectId _id, String username, String password) {
		this._id = _id;
		this.username = username;
		this.password = password;
		this.enabled = false;
		this.isSubcribed = false;
		this.nextRenewalDate = null;
		this.role = "USER";
	}
	
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

	public String get_id() {
		return _id.toString();
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
	
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	
	
}
