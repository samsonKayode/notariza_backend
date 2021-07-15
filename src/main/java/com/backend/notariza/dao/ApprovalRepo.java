package com.backend.notariza.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.ApprovalEntity;

public interface ApprovalRepo extends JpaRepository<ApprovalEntity, Integer> {
	
	//retrieve approval data by status (Approved/Denied)..
	public List<ApprovalEntity> findByStatus(String status);
	
	public ApprovalEntity findByReference(String reference);

}
