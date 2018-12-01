package com.cmpe275.finalproject.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.cmpe275.finalproject.domain.users.UserProfileRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;

import java.io.IOException;

public class AuthenticationService {
  static final long EXPIRATIONTIME = 864_000_00; // 1 day in milliseconds
  static final String SIGNINGKEY = "SecretKey";
  static final String PREFIX = "Bearer";

 
  
  static public void addToken(HttpServletResponse res, String username, List<String> roles) throws IOException {
	//get the roles of user
    String JwtToken = Jwts.builder().setSubject(username)
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
        .claim("role", roles.get(0))
        .signWith(SignatureAlgorithm.HS512, SIGNINGKEY)
        .compact();
    res.addHeader("Authorization", PREFIX + " " + JwtToken);
	res.addHeader("Access-Control-Expose-Headers", "Authorization");
	res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    res.getWriter().write(
            "{\"" + "JWTToken" + "\":\"" + PREFIX + " " + JwtToken + "\","
            		+ "\"role\":\"" + roles.get(0) + "\","
            		+ "\"isEnabled\":" + Boolean.parseBoolean(roles.get(1)) + ","
            		+ "\"profileName\":\"" + roles.get(2) + "\"}"
    );
  }

  static public Authentication getAuthentication(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null) {
    Claims claims = Jwts.parser()         
    		       .setSigningKey(SIGNINGKEY)
    		       .parseClaimsJws(token.replace(PREFIX, "")).getBody();

    
      String user = claims.getSubject();
      String roles = (String)claims.get("role");
      
      if (user != null) { 
    	  //Checking request url with role
    	 if(roles.contains("ADMIN")) {
    		 return new UsernamePasswordAuthenticationToken(user, null, emptyList());
    	 }else if (!request.getRequestURI().contains("/admin")) {
    		 return new UsernamePasswordAuthenticationToken(user, null, emptyList());
    	 }
      }
    }
    return null;
  }
}