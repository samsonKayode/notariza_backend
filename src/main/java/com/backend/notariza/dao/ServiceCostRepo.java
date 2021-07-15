package com.backend.notariza.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.ServiceCostEntity;

public interface ServiceCostRepo extends JpaRepository<ServiceCostEntity, Integer> {
	
	public ServiceCostEntity findByUserType(String userType);

}
