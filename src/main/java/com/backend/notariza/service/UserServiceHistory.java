package com.backend.notariza.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.entity.PaymentEntity;

@Service
public class UserServiceHistory {
	
	@Autowired
	PaymentRepository paymentRepo;
	
	@Autowired
	CurrentUser currentUser;
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	public List<PaymentEntity> getHistory(){
		
		return paymentRepo.findByUserEntityOrderByDateDesc(currentUser.getUserEntity());
	}

}
