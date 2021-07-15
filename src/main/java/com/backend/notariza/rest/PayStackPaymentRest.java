package com.backend.notariza.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.paystack.VerifyTransactionResponse;
import com.backend.notariza.service.custom.InitPaymentService;

@RestController
@CrossOrigin
@RequestMapping("/v1/payment")
public class PayStackPaymentRest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	InitPaymentService initPaymentService;

	@GetMapping("/verifytransaction/{reference}")
	public ResponseEntity<?> verifyPayment(@PathVariable String reference)throws Exception {

		VerifyTransactionResponse response = null;
		
		log.info("PAYSTACK REST REFEFENCE====>"+reference);

		try {

			response = initPaymentService.verifyTransaction(reference);
		} catch (Exception ee) {
			System.out.println("Error verifying payment");
			log.info("PSTK REST REFEFENCE====>"+reference);
			
			ee.printStackTrace();
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
