package com.cmpe275.finalproject.domain.users;

public class UserProfileTypeStats {
	
	int numberOfUsers;

	String typeOfUser;

	
	public UserProfileTypeStats(String typeOfUser,int numberOfUsers ) {
		this.numberOfUsers = numberOfUsers;
		this.typeOfUser = typeOfUser;
	}

	public String getTypeOfUser() {
		return typeOfUser;
	}

	public void setTypeOfUser(String typeOfUser) {
		this.typeOfUser = typeOfUser;
	}
	
	public int getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

}
