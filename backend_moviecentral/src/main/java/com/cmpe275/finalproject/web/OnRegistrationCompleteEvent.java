package com.cmpe275.finalproject.web;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.cmpe275.finalproject.domain.users.UserProfile;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private String appUrl;
    private Locale locale;
    private UserProfile userProfile;
 
    public OnRegistrationCompleteEvent(
    		UserProfile userProfile, Locale locale, String appUrl) {
        super(userProfile);
         
        this.userProfile = userProfile;
        this.locale = locale;
        this.appUrl = appUrl;
    }

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
     
}
