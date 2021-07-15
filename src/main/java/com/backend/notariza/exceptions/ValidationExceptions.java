package com.backend.notariza.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptions {
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
			
			log.error(fieldName+": "+errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
	}

	// General Exception..

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleSQLExceptions(Exception ex) {

		Map<String, String> errors = new HashMap<>();

		errors.put("error_message", ex.getMessage());
		errors.put("error_type", ex.getClass().getName());
		
		log.error(ex.getLocalizedMessage());
		
		//ex.printStackTrace();

		return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
	}
	
	//Verification Expired exception...
	
	@ExceptionHandler(VerificationExpiredException.class)
	public ResponseEntity<Object> verificationExpired(VerificationExpiredException ex) {

		Map<String, String> errors = new HashMap<>();

		errors.put("error_message","Verification link is either invalid or expired");
		
		log.error("verification done exception");
		
		//ex.printStackTrace();

		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	}
	
	//username exist..
	
	@ExceptionHandler(UsernameExistException.class)
	public ResponseEntity<Object> usernameExist(UsernameExistException ex) {

		Map<String, String> errors = new HashMap<>();

		errors.put("error_message","user already exist");
		
		log.error("username exists exception");
		
		//ex.printStackTrace();

		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ReferenceNotFound.class)
	public ResponseEntity<Object> referenceError(ReferenceNotFound ex) {

		Map<String, String> errors = new HashMap<>();

		errors.put("error_message","invalid reference");
		
		log.error("reference exception");
		
		//ex.printStackTrace();

		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<Object> referenceError(PaymentException ex) {

		Map<String, String> errors = new HashMap<>();

		errors.put("error_message","Payment already processed");
		
		log.error("Payment already processed exception");
		

		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	}
	
}
