package com.backend.notariza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.EmailSettingRepo;
import com.backend.notariza.entity.EmailSettingEntity;

@Service
public class EmailSettingService {
	
	@Autowired
	EmailSettingRepo emailRepo;
	
	//Save Email..
	
	public EmailSettingEntity saveEmail(EmailSettingEntity emailSetting) {
		
		return emailRepo.save(emailSetting);
	}
	
	//update email
	
	public EmailSettingEntity updateEmail(EmailSettingEntity emailSetting, int id) {
		
		EmailSettingEntity emailSettingEntity = emailRepo.getById(id);
		
		emailSettingEntity.setHostname(emailSetting.getHostname());
		emailSettingEntity.setPassword(emailSetting.getPassword());
		emailSettingEntity.setPort(emailSetting.getPort());
		emailSetting.setUsername(emailSetting.getUsername());
		
		return emailRepo.save(emailSettingEntity);
		
	}
	
	//retrieve email setting..
	
	public EmailSettingEntity getEmail(int id) {
		
		return emailRepo.getById(id);
	}

}
