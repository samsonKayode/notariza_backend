package com.backend.notariza.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.RoleRepository;
import com.backend.notariza.entity.Role;

@Service
public class RolesService {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	RoleRepository roleRepo;
	
	//create new Roles..
	
	public Role saveRole(Role role){
		log.info("Saving new Role");
		return roleRepo.save(role);
	}
	
	//list all roles
	
	public List<Role> getAllRoles(){
		
		log.info("Getting all Roles");
		return roleRepo.findAll();
	}

}
