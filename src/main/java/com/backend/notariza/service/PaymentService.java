package com.backend.notariza.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.dto.AwaitingVerificationDTO;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.UserEntity;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepo;

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	Date date;

	String service;

	String reference;

	String customerName;

	int customerID;

	int sn = 0;

	// get payment by date..

	public List<PaymentEntity> getPaymentByDate(Date startDate, Date endDate) {

		return paymentRepo.findByDateBetween(startDate, endDate);

	}

	public PaymentEntity getByReference(String reference) {

		return paymentRepo.findByReference(reference);
	}

	public List<PaymentEntity> getService(String service) {

		return paymentRepo.findByService(service);
	}

	// searching data...

	public List<PaymentEntity> findAllData(String service, String reference, Date startDate, Date endDate)
			throws Exception {

		return paymentRepo.findByServiceAndReferenceAndDate(service, reference, startDate, endDate);
	}

	// search data with customer name in the result....

	public List<AwaitingVerificationDTO> findAllData1(String service, String reference, Date startDate, Date endDate)
			throws Exception {
		
		AwaitingVerificationDTO awaitingVerification;
		
		ArrayList<AwaitingVerificationDTO> arrayList = new ArrayList<AwaitingVerificationDTO>();
		
		List<PaymentEntity> list1 = paymentRepo.findByServiceAndReferenceAndDate(service, reference, startDate, endDate);

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

}
