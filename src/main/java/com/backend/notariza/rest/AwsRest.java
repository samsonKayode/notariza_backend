package com.backend.notariza.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.pojos.ActionResult;
import com.backend.notariza.service.AwsAccessVerification;
import com.backend.notariza.service.custom.AWSS3Service;

@RestController
@CrossOrigin
@RequestMapping()
public class AwsRest {
	
	@Autowired
    private AWSS3Service awsService;
	
	@Autowired
	AwsAccessVerification awsVerification;
	
	
	//display pdf file on browser..
    @GetMapping(value= "/v1/service/files/view")
    public ResponseEntity<?> viewFile(@RequestParam(value= "fileName") final String keyName) {
    	
    	String status = awsVerification.check(keyName);
    	
    	if(status.equals("GRANTED")) {
    		
    		final byte[] data = awsService.downloadFile(keyName);
    		
            final ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type", "application/pdf")
                    .header("Content-disposition", "inline; filename=\"" + keyName + "\"")
                    .body(resource);
    	}else {
    		
    		ActionResult ar = new ActionResult(1,"Authorization Error: YOU ARE NOT ALLOWED TO ACCESS THIS FILE", System.currentTimeMillis());
    		
    		return ResponseEntity.ok().body(ar);
    	}
    }
    
    //download file for users
    @GetMapping(value= "/v1/service/files/download")
    public ResponseEntity<ByteArrayResource> downloadFileUser(@RequestParam(value= "fileName") final String keyName) {
        final byte[] data = awsService.downloadFile(keyName);
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/pdf")
                .header("Content-disposition", "attachment; filename=\"" + keyName + "\"")
                .body(resource);
    }
    
    //Display file for admin...
    @GetMapping(value= "/v1/admin/files/view")
    public ResponseEntity<?> viewFileAdmin(@RequestParam(value= "fileName") final String keyName) {

    		
    		final byte[] data = awsService.downloadFile(keyName);
            final ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type", "application/pdf")
                    .header("Content-disposition", "inline; filename=\"" + keyName + "\"")
                    .body(resource);
    		
    	}
    
    //download file...
    @GetMapping(value= "/v1/admin/files/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value= "fileName") final String keyName) {
        final byte[] data = awsService.downloadFile(keyName);
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/pdf")
                .header("Content-disposition", "attachment; filename=\"" + keyName + "\"")
                .body(resource);
    }

}
