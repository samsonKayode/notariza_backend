package com.backend.notariza.rest;

import javax.validation.Valid;

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

import com.backend.notariza.entity.GiftDeedEntity;
import com.backend.notariza.exceptions.ReferenceNotFound;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.service.CurrentUser;
import com.backend.notariza.service.GiftDeedService;

@RestController
@CrossOrigin
@RequestMapping("/v1/service/giftdeed")
public class GiftDeedRest {

	@Autowired
	private GiftDeedService giftService;

	@Autowired
	private CurrentUser currentUser;

	@Value("${payment_message2}")
	private String message;

	@Value("${payment_confirmation_url}")
	private String url;

	@Value("${paystack.failure}")
	private String failure;

	Logger log = LoggerFactory.getLogger(this.getClass());

	// make payment
	@PostMapping("/makepayment")
	public InitializeTransactionResponse makePayment(@Valid @RequestBody GiftDeedEntity giftDeedEntity)
			throws MethodArgumentNotValidException, Exception {

		return giftService.makePayment(giftDeedEntity, currentUser.getUserId());

	}

	// verify to save..

	@GetMapping("/savetransaction/{reference}")
	public RedirectView verifyPaymentToSave(@PathVariable String reference) throws ReferenceNotFound, Exception {

		RedirectView redirectView = new RedirectView();

		try {

			giftService.verifyTransactionToSave(reference);

			redirectView.setUrl(url + "/payment-successful?message=" + message);

		} catch (Exception ex) {

			log.error("Error verifying payment ==>" + reference);

			log.error(reference + " ==> " + ex.getLocalizedMessage());
			redirectView.setUrl(url + "/payment-failed");
		}

		return redirectView;
	}

	// find by refetence..
	@GetMapping("/find")
	public GiftDeedEntity getByReference(@RequestParam(value = "reference") final String reference) throws Exception {

		return giftService.getByReference(reference);
	}

}
