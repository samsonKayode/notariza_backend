package com.backend.notariza.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.NotarizeDocumentEntity;

public interface NotarizeDocumentRepo extends JpaRepository<NotarizeDocumentEntity, Integer> {
	
	public NotarizeDocumentEntity findByReference(String reference);
	
	public Boolean existsByReference(String reference);

}
