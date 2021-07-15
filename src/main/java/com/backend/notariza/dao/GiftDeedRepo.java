package com.backend.notariza.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.GiftDeedEntity;

public interface GiftDeedRepo extends JpaRepository<GiftDeedEntity, Integer> {
	
	public GiftDeedEntity findByReference(String reference);
	
	public Boolean existsByReference(String reference);

}
