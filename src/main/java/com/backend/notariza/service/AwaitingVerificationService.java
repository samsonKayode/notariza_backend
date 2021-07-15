package com.backend.notariza.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.ApprovalRepo;
import com.backend.notariza.dao.GiftDeedRepo;
import com.backend.notariza.dao.NotarizeDocumentRepo;
import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.dto.AwaitingVerificationDTO;
import com.backend.notariza.entity.ApprovalEntity;
import com.backend.notariza.entity.GiftDeedEntity;
import com.backend.notariza.entity.NotarizeDocumentEntity;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.UserEntity;

@Service
public class AwaitingVerificationService {

	@Autowired
	PaymentRepository paymentRepo;

	@Autowired
	GiftDeedRepo giftRepo;

	@Autowired
	NotarizeDocumentRepo notarizeRepo;

	@Autowired
	ApprovalRepo approvalRepo;

	@Autowired
	CurrentUser currentUser;
	
	@Autowired
	SendEmailService sendEmail;

	Logger log = LoggerFactory.getLogger(this.getClass());

	Date date;

	String service;

	String reference;

	String customerName;

	int customerID;

	int sn = 0;

	// Retrieve all pending video confirmation...

	public List<AwaitingVerificationDTO> getAllPending() {

		AwaitingVerificationDTO awaitingVerification;

		ArrayList<AwaitingVerificationDTO> arrayList = new ArrayList<AwaitingVerificationDTO>();

		List<PaymentEntity> list1 = paymentRepo.findByStatus("Awaiting Video Verification");

		for (int i = 0; i < list1.size(); i++) {

			sn++;

			PaymentEntity paymentEntity = list1.get(i);

			date = paymentEntity.getDate();
			service = paymentEntity.getService();
			reference = paymentEntity.getReference();

			UserEntity userEntity = paymentEntity.getUserEntity();

			String firstName = userEntity.getFirstname();
			String otherName = userEntity.getOthernames();
			String lastName = userEntity.getLastname();

			customerName = firstName + " " + otherName + " " + lastName;
			customerID = userEntity.getId();

			awaitingVerification = new AwaitingVerificationDTO();

			awaitingVerification.setCustomerName(customerName);
			awaitingVerification.setCustomerID(customerID);
			awaitingVerification.setReference(reference);
			awaitingVerification.setDate(date);
			awaitingVerification.setService(service);
			awaitingVerification.setId(sn);

			arrayList.add(awaitingVerification);

		}

		return arrayList;
	}

	// approve verification gift deed table..

	public void updateGiftDeed(String reference, String status) {

		final String status1;

		GiftDeedEntity GDE = giftRepo.findByReference(reference);

		if (status.equalsIgnoreCase("yes")) {
			status1 = "APPROVED";
		} else {
			status1 = "DENIED";
		}

		GDE.setStatus(status1);
		log.info("Gift deed status updated");

		giftRepo.save(GDE);

	}

	// approve verification notarize_documents table..

	public void updateNotarizeDocument(String reference, String status) {

		final String status1;
		NotarizeDocumentEntity NDE = notarizeRepo.findByReference(reference);

		if (status.equalsIgnoreCase("yes")) {
			status1 = "APPROVED";
		} else {
			status1 = "DENIED";
		}

		NDE.setStatus(status1);
		log.info("Notarize document status updated");

		notarizeRepo.save(NDE);
	}

	// save approval entity data here..

	@Async
	public ApprovalEntity saveApprovalEntity(ApprovalEntity approvalEntity) {

		return approvalRepo.save(approvalEntity);
	}

	// approve verification payment table..

	public PaymentEntity updatePaymentEntity(String reference, String status) throws Exception {
		
		String status1="";

		PaymentEntity paymentEntity = paymentRepo.findByReference(reference);
		
		UserEntity userEntity = paymentEntity.getUserEntity();
		
		String userEmail = userEntity.getUsername();
		String fullname = userEntity.getFirstname()+" "+userEntity.getLastname();
		

		String service = paymentEntity.getService();

		if (service.equals("Financial Gift Deed")) {

			updateGiftDeed(reference, status);
		} else {
			updateNotarizeDocument(reference, status);
		}
		
		if (status.equalsIgnoreCase("yes")) {
			status1 = "APPROVED";
		} else {
			status1 = "DENIED";
		}
		
		paymentEntity.setStatus(status1);
		paymentRepo.save(paymentEntity);

		// save approval entity..
		ApprovalEntity approvalEntity = new ApprovalEntity();

		approvalEntity.setDate(new Date());
		approvalEntity.setReference(reference);
		approvalEntity.setStatus(status);
		approvalEntity.setUserEntity(currentUser.getUserEntity());

		saveApprovalEntity(approvalEntity);
		
		//send verificationemail..
		
		try {
			sendEmail.verificationDone(userEmail, status, fullname);

		}catch(Exception nn) {
			
		}
		return paymentEntity;
	}

}
