package com.backend.notariza.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.UserEntity;

public interface UserDao extends JpaRepository<UserEntity, Integer> {
	
	public UserEntity findByUsername(String username);
	
	public UserEntity findById(int id);
	
	public List<UserEntity> findByUserType(String userType);
	
	public UserEntity findByVerificationCodeAndEnabled(String verificationCode, boolean enabled);
	
	public Boolean existsByUsername(String username);
}
