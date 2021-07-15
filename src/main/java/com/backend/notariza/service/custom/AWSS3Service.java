package com.backend.notariza.service.custom;

import java.io.File;

public interface AWSS3Service {

	//public void uploadFile(final MultipartFile multipartFile);
	public void uploadFile(final File multipartFile);
	
	public byte[] downloadFile(final String keyName);
		
}
