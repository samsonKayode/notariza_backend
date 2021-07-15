package com.backend.notariza.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;


public class NotarizeDocumentDTO {
	
	@NotBlank(message = "document title is mandatory")
	@Size(min=5)
	private String title;
	
	private String agent_code;
	
	@NotNull(message = "you must upload a file")
	private MultipartFile file;
	
	@NotNull(message = "ctc is mandatory")
	private boolean ctc;
	
	public NotarizeDocumentDTO() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAgent_code() {
		return agent_code;
	}

	public void setAgent_code(String agent_code) {
		this.agent_code = agent_code;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public boolean isCtc() {
		return ctc;
	}

	public void setCtc(boolean ctc) {
		this.ctc = ctc;
	}
	
}
