package com.backend.notariza.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.entity.EmailSettingEntity;
import com.backend.notariza.service.EmailSettingService;


@RestController
@CrossOrigin
@RequestMapping("/v1/admin/settings/email")
public class EmailRest {
	
	@Autowired
	EmailSettingService emailService;
	
	Logger log = LoggerFactory.getLogger(EmailRest.class);
	
	//save email data..
	@PostMapping("/save")
	public EmailSettingEntity saveEmail(@RequestBody EmailSettingEntity email) {
	
		return emailService.saveEmail(email);
	}
	
	//Email setting id is hardcoded to 1
	@PutMapping("/update")
	public EmailSettingEntity updateEmail(@RequestBody EmailSettingEntity email) {
		
		return emailService.updateEmail(email, 1);
	}

	
	/*
	 * I am using 1 as the value cos the database table has a constraint that makes sure only one row is inserted into the table and it has a default id as 1
	 */

	//get Single record..
	@GetMapping("/email")
	public EmailSettingEntity getEmail() {
		
		
		return emailService.getEmail(1);
	}
	
	
}
