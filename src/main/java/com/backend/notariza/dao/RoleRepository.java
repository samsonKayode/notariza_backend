package com.backend.notariza.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	public Role findByName(String role);

}