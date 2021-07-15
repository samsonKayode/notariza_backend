package com.backend.notariza.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.entity.Role;
import com.backend.notariza.service.RolesService;

@RestController
@CrossOrigin
@RequestMapping("/v1/admin/roles")
public class RoleRest {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	RolesService roleService;
	
	//save role..
	@PostMapping("/save")
	public Role saveRole(@RequestBody Role role) throws Exception {
		
		return roleService.saveRole(role);
	}
	
	//get all roles..
	@GetMapping("/list")
	public List<Role> getAll() throws Exception{
		
		return roleService.getAllRoles();
	}
	
}
