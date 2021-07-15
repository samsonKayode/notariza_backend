package com.backend.notariza.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.dto.AwaitingVerificationDTO;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.service.AwaitingVerificationService;

@RestController
@CrossOrigin
@RequestMapping("/v1/admin/requests/pending")
public class AwaitingVerificationRest {
	
	@Autowired
	AwaitingVerificationService awaitingService;
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	//list awaiting video verification list..
	@GetMapping("/list")
	public List<AwaitingVerificationDTO> listAllPending() throws Exception{
		
		return awaitingService.getAllPending();
	}
	
	//Approve or deny request...
	@GetMapping("/save")
	public PaymentEntity approveDenyRequest(@RequestParam(value= "reference") final String reference, @RequestParam(value= "status") final String status) 
			throws Exception {
		
		return awaitingService.updatePaymentEntity(reference, status);
		
	}

}
