package com.backend.notariza.rest;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.backend.notariza.entity.AgeDeclarationEntity;
import com.backend.notariza.exceptions.ReferenceNotFound;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.service.AgeDeclarationService;
import com.backend.notariza.service.CurrentUser;

@RestController
@CrossOrigin
@RequestMapping("/v1/service/ageDeclaration")
public class AgeDeclarationRest {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AgeDeclarationService ageDeclarationService;

	@Autowired
	CurrentUser currentUser;

	@Value("${payment_message}")
	private String message;

	@Value("${payment_confirmation_url}")
	private String url;

	@Value("${paystack.failure}")
	private String failure;

	Logger log = LoggerFactory.getLogger(this.getClass());

	// make payment..
	@PostMapping("/makepayment")
	public InitializeTransactionResponse makePayment(@RequestBody AgeDeclarationEntity ageDeclarationEntity)
			throws MethodArgumentNotValidException, Exception {

		InitializeTransactionResponse response = null;

		response = ageDeclarationService.makePayment(ageDeclarationEntity, currentUser.getUserId());

		return response;
	}

	
	// Verify and save transactions...
	@GetMapping("/savetransaction/{reference}")
	public RedirectView verifyPaymentToSave(@PathVariable String reference) throws FileNotFoundException, ReferenceNotFound, Exception {

		RedirectView redirectView = new RedirectView();

		try {

			ageDeclarationService.callBackURL(reference);

			redirectView.setUrl(url + "/payment-successful?message=" + message);

		} catch (Exception ee) {
			log.error("Error verifying payment ==>" + reference);
			log.error(reference+" ==> "+ee.getLocalizedMessage());

			redirectView.setUrl(url + "/payment-failed");
		}

		return redirectView;

	}
	

	// get by reference..
	@GetMapping("/find")
	public AgeDeclarationEntity getByRef(@RequestParam(value = "reference") final String reference) throws Exception {

		return ageDeclarationService.getByReference(reference);
	}

}
