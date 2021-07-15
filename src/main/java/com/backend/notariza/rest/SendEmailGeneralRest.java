package com.backend.notariza.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.service.SendEmailService;

@RestController
@CrossOrigin
@RequestMapping("/v1/service/email")
public class SendEmailGeneralRest {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SendEmailService sendEmailService;
	
	//send email..


	

}
