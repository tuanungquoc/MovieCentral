package com.cmpe275.finalproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.cmpe275.finalproject.domain.users.AccountCredentials;
import com.cmpe275.finalproject.domain.users.UserProfile;
import com.cmpe275.finalproject.domain.users.UserProfileRepository;
import com.cmpe275.finalproject.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

  public LoginFilter(String url, AuthenticationManager authManager) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  @Autowired
  private UserProfileRepository repository;
  
  @Override
  public Authentication attemptAuthentication(
	HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
	  AccountCredentials creds = new ObjectMapper()
        .readValue(req.getInputStream(), AccountCredentials.class);
	  
	return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(
            creds.getUsername(),
            creds.getPassword(),
            Collections.emptyList()
        )
    );
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest req,
      HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {
	  String username = auth.getName();
	  List<String> roles = new ArrayList();
	  Iterator<GrantedAuthority> iter = (Iterator<GrantedAuthority>) auth.getAuthorities().iterator();
      while(iter.hasNext()) {
    	  GrantedAuthority grantedAuth = iter.next();
    	  roles.add(grantedAuth.getAuthority());
      }
	  AuthenticationService.addToken(res, auth.getName(), roles);
  }
}