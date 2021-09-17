package com.backend.notariza.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.RoleRepository;
import com.backend.notariza.dao.UserDao;
import com.backend.notariza.entity.Role;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.exceptions.UsernameExistException;
import com.backend.notariza.exceptions.VerificationExpiredException;

import net.bytebuddy.utility.RandomString;


@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private RoleRepository roleRepo;
	
	private UserEntity user;
	
	@Autowired
	SendEmailService sendEmail;
	
	List<SimpleGrantedAuthority> authorities;
	

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
         
        return authorities;
    }
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userDao.findByUsername(username);
		
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
		 Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
           authorities.add(new SimpleGrantedAuthority(role.getName()));
       }
		
		//return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),true, true, true, authorities);
	}

	public UserEntity save(UserEntity user) throws UsernameExistException, Exception {
		
		if(userDao.existsByUsername(user.getUsername())) {
			throw new UsernameExistException();
		}else {
		
		UserEntity newUser = new UserEntity();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		
		newUser.setAddress(user.getAddress());
		newUser.setSex(user.getSex());
		newUser.setFirstname(user.getFirstname());
		newUser.setLastname(user.getLastname());
		newUser.setDob(user.getDob());
		newUser.setOthernames(user.getOthernames());
		
		String code = RandomString.make(164);
		
		newUser.setVerificationCode(code);
		
		newUser.setDate(new Date());
		
		newUser.setEnabled(false);
		
		 if(user.getUserType()==null) {
			newUser.setUserType("GENERAL USER");
			
			Set<Role> arrayList = new HashSet<Role>();
			 
			 Role newRole = roleRepo.findByName("GENERAL USER");
			 
			 arrayList.add(newRole);

			 newUser.setRoles(arrayList);
		}else {
			
			newUser.setUserType(user.getUserType());
		
			Set<Role> arrayList = new HashSet<Role>();
			Role newRole = roleRepo.findByName(user.getUserRoleName());
			arrayList.add(newRole);
			newUser.setRoles(arrayList);
			
		}
		 
			String fullname = newUser.getFirstname()+" "+newUser.getOthernames()+" "+newUser.getLastname();
			String  to = newUser.getUsername();
		
		userDao.save(newUser);
		
		//send email..
		
		sendEmail.accountVerificationEmail(to, fullname, code);
		
		return  newUser;
		}
	}
	
	//verify  account..
	
	public UserEntity verifyAccount(String verificationCode) throws Exception {
		
		UserEntity userEntity  = userDao.findByVerificationCodeAndEnabled(verificationCode, false);
		
		if(userEntity==null) throw new VerificationExpiredException();
		
		userEntity.setEnabled(true);
		
		userDao.save(userEntity);
		
		
		return userEntity;
		
	}
	
	//update data..
	
	public UserEntity update(UserEntity user, int id) {
		
		UserEntity newUser = userDao.getById(id);
		
		//UserEntity newUser = new UserEntity();
		//newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		
		
		newUser.setAddress(user.getAddress());
		newUser.setSex(user.getSex());
		newUser.setFirstname(user.getFirstname());
		newUser.setLastname(user.getLastname());
		newUser.setDob(user.getDob());
		newUser.setOthernames(user.getOthernames());
		
		 if(user.getUserType()==null) {
			newUser.setUserType("GENERAL USER");
			
			Set<Role> arrayList = new HashSet<Role>();
			 
			 Role newRole = roleRepo.findByName("GENERAL USER");
			 
			 arrayList.add(newRole);

			 newUser.setRoles(arrayList);
		}else {
			
			newUser.setUserType(user.getUserType());
		
			Set<Role> arrayList = new HashSet<Role>();
			Role newRole = roleRepo.findByName(user.getUserRoleName());
			arrayList.add(newRole);
			newUser.setRoles(arrayList);
			
		}
		

		return  userDao.save(newUser);
	}
	
	//delete user..
	
	public int deleteUser(int id) {
		
		int a =1;
		
		UserEntity UE = userDao.getById(id);
		
		if(UE.getUsername().length()>1) {
			//lets delete ..
			userDao.deleteById(id);
			a = 0;
		}
		
		return a;
	}
	
	//return one user by username..
	
	public UserEntity getOneByUsername (String username) {
		
		return userDao.findByUsername(username);
	}
	
	//return one user by id..
	
	public UserEntity getOneById (int id) {
		
		return userDao.findById(id);
	}
	
	//return list of all users..
	
	public List<UserEntity> getAllUsers(){
		
		return userDao.findAll();
	}

}