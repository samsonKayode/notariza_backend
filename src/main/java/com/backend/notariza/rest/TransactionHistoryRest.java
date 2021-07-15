package com.backend.notariza.rest;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.dto.AwaitingVerificationDTO;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.service.PaymentService;

@RestController
@CrossOrigin
@RequestMapping("/v1/admin/logs")
public class TransactionHistoryRest {

	@Autowired
	private PaymentService paymentService;

	Logger log = LoggerFactory.getLogger(this.getClass());

	// get sorted Data;
	@GetMapping("/list")
	public List<PaymentEntity> getData(@RequestParam(value = "startDate") final Date startDate,
			@RequestParam(value = "endDate") final Date endDate) {

		return paymentService.getPaymentByDate(startDate, endDate);
	}

	// get all services..
	@GetMapping("/services")
	public List<PaymentEntity> getServiceData(@RequestParam(value = "serviceName") final String service) {

		return paymentService.getService(service);
	}

	// searching data..
	@GetMapping("/search")
	public List<AwaitingVerificationDTO> searchData(@RequestParam(value = "service") String service,
			@RequestParam(value = "reference") String reference, @RequestParam(value = "startDate") Date startDate,
			@RequestParam(value = "endDate") Date endDate) throws Exception {

		if (reference.isEmpty()) {
			reference = null;
		}

		if (service.isEmpty()) {
			service = null;
		}

		if (startDate.toString().isEmpty()) {

			startDate = null;
			endDate = null;

		}

		return paymentService.findAllData1(service, reference, startDate, endDate);
	}

}
