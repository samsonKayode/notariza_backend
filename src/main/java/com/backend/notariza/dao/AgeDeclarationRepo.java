package com.backend.notariza.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.AgeDeclarationEntity;

public interface AgeDeclarationRepo extends JpaRepository<AgeDeclarationEntity, Integer> {
	
	public AgeDeclarationEntity findByReference(String reference);
	
	public Boolean existsByReference(String reference);

}
