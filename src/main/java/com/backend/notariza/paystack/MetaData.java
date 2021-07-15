package com.backend.notariza.paystack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaData {
	
	private String service;
	
	//private String phone;
	
	public MetaData() {
		
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	


}
