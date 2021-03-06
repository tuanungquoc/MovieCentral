package com.cmpe275.finalproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	private UserProfileRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{ 
		UserProfile currentUser = repository.findByUsername(username);
		String[] userInfo = {currentUser.getRole(), currentUser.isEnabled()+"", currentUser.getProfileName(),currentUser.get_id()};
		UserDetails user = new org.springframework.security.core
				.userdetails.User(username, currentUser.getPassword()
						, true, true, true, true, 
						AuthorityUtils.createAuthorityList("\"role\" : \"" + currentUser.getRole() + "\"",
								"\"isEnable\" : " + currentUser.isEnabled()+"",
								"\"profileName\" :\"" + currentUser.getProfileName() + "\"",
								"\"userId\":\"" + currentUser.get_id()+"\""));
		return user;
	}

}