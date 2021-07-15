package com.backend.notariza.rest;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.backend.notariza.dto.NotarizeDocumentDTO;
import com.backend.notariza.entity.NotarizeDocumentEntity;
import com.backend.notariza.exceptions.PaymentException;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.service.CurrentUser;
import com.backend.notariza.service.NotarizeDocumentService;

@RestController
@CrossOrigin
@RequestMapping("/v1/service/notarizedocument")
public class NotarizeDocumentRest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CurrentUser currentUser;

	@Autowired
	private NotarizeDocumentService notarizeService;

	@Value("${payment_message2}")
	private String message;

	@Value("${payment_confirmation_url}")
	private String url;

	@Value("${paystack.failure}")
	private String failure;

	// make payment api...
	@PostMapping("/makepayment")
	public InitializeTransactionResponse makePayment(@Valid @ModelAttribute NotarizeDocumentDTO notarizeDTO)
			throws MethodArgumentNotValidException, Exception {

		InitializeTransactionResponse response = null;

		response = notarizeService.makePayment(notarizeDTO, currentUser.getUserId());

		return response;
	}

	// verify and update transaction..
	@GetMapping("/savetransaction/{reference}")
	public RedirectView verifyPaymentToSave(@PathVariable String reference) throws PaymentException, Exception {

		RedirectView redirectView = new RedirectView();

		try {
			notarizeService.verifyTransactionToSave(reference);
			redirectView.setUrl(url + "/payment-successful?message=" + message);

		} catch (Exception ex) {
			log.error("Error verifying payment ==>" + reference);

			log.error(reference+" ==> "+ex.getLocalizedMessage());ex.printStackTrace();
			redirectView.setUrl(url + "/payment-failed");
		}

		return redirectView;
	}

	// find by reference...
	@GetMapping("/find")
	public NotarizeDocumentEntity findByRef(@RequestParam(value = "reference") final String reference)
			throws Exception {

		return notarizeService.getByReference(reference);
	}

}
