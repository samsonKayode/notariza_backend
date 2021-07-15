package com.backend.notariza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.UserDao;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.jwt.JwtTokenUtil;

@Service
public class CurrentUser {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	
	public String getUsername() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String username="";
		
		if (principal instanceof UserDetails) {
			   username = ((UserDetails)principal).getUsername();
			} else {
			   username = principal.toString();
			}
		
		return username;
	}
	
	public int getUserId() {
		
		UserEntity userEntity = userDao.findByUsername(getUsername());
		
		return userEntity.getId();
		
	}
	
	public UserEntity getUserEntity() {
		UserEntity userEntity = userDao.findByUsername(getUsername());
		
		return userEntity;
	}
	
	public UserDetails  getUserDetails(Authentication authentication) {
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		return userDetails;

	}
	
	//get token...
	
	public String getToken() {
	    String token = null;
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null) {
			 UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername());
			 token = jwtTokenUtil.generateToken(userDetails);
	    }
	    return token;
	  }

}
