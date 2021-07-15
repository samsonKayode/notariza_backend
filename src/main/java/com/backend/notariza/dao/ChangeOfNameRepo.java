package com.backend.notariza.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.ChangeOfNameEntity;

public interface ChangeOfNameRepo extends JpaRepository<ChangeOfNameEntity, Integer> {

	public ChangeOfNameEntity findByReference(String reference);
	
	public Boolean existsByReference(String reference);

}
