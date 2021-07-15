package com.backend.notariza.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.UserEntity;

@Service
public class AwsAccessVerification {
	
	@Autowired
	PaymentRepository paymentRepo;
	
	@Autowired
	CurrentUser currentUser;

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	/*check if reference number is connected to the user..
	 *Also check if the reference number is denied or approved
	 */
	public String check(String fileName) {
		
		String reference;
		
		if(fileName.contains("_certificate")) {
			reference = fileName.substring(0, fileName.length() - 16);
		}else {
			reference = fileName.substring(0, fileName.length() - 4);
		}
		
		log.info("REFERENCE======>>>>"+reference);
		
		String str="";
		
		PaymentEntity paymentEntity = paymentRepo.findByReference(reference);
		
		UserEntity userEntity = paymentEntity.getUserEntity();
		
		int loggedInID=currentUser.getUserId();
		int userID = userEntity.getId();
		String status = paymentEntity.getStatus();
		
		if(status.equals("Awaiting Video Verification")||status.equals("DENIED")||loggedInID!=userID) {
			str="DENIED";
			log.info("Status: "+status+"=== >ACCESS SHOULD BE DENIED");
		}else {
			str = "GRANTED";
			log.info("Status: "+status+"=== >ACCESS SHOULD BE GRANTED");
		}

		return str;
	}
	
	
	
}
