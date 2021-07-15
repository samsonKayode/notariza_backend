package com.backend.notariza.rest;

import java.io.FileNotFoundException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.backend.notariza.entity.ChangeOfNameEntity;
import com.backend.notariza.exceptions.ReferenceNotFound;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.service.ChangeOfNameService;
import com.backend.notariza.service.CurrentUser;

@RestController
@CrossOrigin
@RequestMapping("/v1/service/changeofname")
public class ChangeOfNameRest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${payment_message}")
	private String message;

	@Value("${payment_confirmation_url}")
	private String url;

	@Value("${paystack.failure}")
	private String failure;

	@Autowired
	ChangeOfNameService changeOfNameService;

	@Autowired
	CurrentUser currentUser;

	// Paystack...
	@PostMapping("/makepayment")
	public ResponseEntity<?> makePayment(@Valid @RequestBody ChangeOfNameEntity changeOfNameEntity)
			throws MethodArgumentNotValidException, Exception {

		InitializeTransactionResponse response = null;

		response = changeOfNameService.makePayment(changeOfNameEntity, currentUser.getUserId());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Using redirect view...

	@GetMapping("/savetransaction/{reference}")
	public RedirectView verifyPaymentToSave(@PathVariable String reference)
			throws FileNotFoundException, ReferenceNotFound, Exception {

		RedirectView redirectView = new RedirectView();
		try {

			changeOfNameService.verifyTransactionToSave(reference);
			redirectView.setUrl(url + "/payment-successful?message=" + message);

		} catch (Exception ee) {
			log.error("Error verifying payment ==>" + reference);

			log.error(reference + " ==> " + ee.getLocalizedMessage());
			redirectView.setUrl(url + "/payment-failed");
		}

		return redirectView;

	}

	// get change of name data by reference...
	@GetMapping("/find")
	public ChangeOfNameEntity getByReference(@RequestParam(value = "reference") final String reference)
			throws Exception {

		return changeOfNameService.getOne(reference);
	}

}
