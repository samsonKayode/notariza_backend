package com.backend.notariza.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.GiftDeedRepo;
import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.dao.ServiceCostRepo;
import com.backend.notariza.dao.UserDao;
import com.backend.notariza.entity.GiftDeedEntity;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.ServiceCostEntity;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.exceptions.ReferenceNotFound;
import com.backend.notariza.paystack.Data;
import com.backend.notariza.paystack.InitializeTransactionRequest;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.paystack.VerifyTransactionResponse;
import com.backend.notariza.service.custom.AWSS3Service;
import com.backend.notariza.util.GiftDeedPDF;
import com.backend.notariza.util.RandomReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

@Service
public class GiftDeedService {

	@Autowired
	GiftDeedRepo giftRepo;

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${notary_name}")
	private String notaryName;

	@Autowired
	private UserDao userDao;

	RandomReference randomReference = new RandomReference();

	private static final int STATUS_CODE_OK = 200;
	@Value("${paystack.testkey}")
	private String secretKey;

	@Value("${base_url}")
	private String base_url;

	@Autowired
	private CurrentUser currentUser;

	Data data;

	@Autowired
	PaymentRepository paymentRepo;

	@Autowired
	ServiceCostRepo serviceRepo;

	int serviceCost = 0;

	GiftDeedEntity giftDeedEntity;
	int gd_id = 0;

	public String tmpDirsLocationD = System.getProperty("java.io.tmpdir") + "/";

	GiftDeedPDF giftDeedPDF;

	@Autowired
	private AWSS3Service awsService;

	@Autowired
	SendEmailService sendEmail;

	@Autowired
	RestTemplate restTemplate;
	
	String referenceNumber=null;

	// Make payment...

	public InitializeTransactionResponse makePayment(GiftDeedEntity gdEntity, int user_id) {
		
		referenceNumber = randomReference.getAlphaNumericString(40);

		giftDeedEntity = new GiftDeedEntity();

		giftDeedEntity.setTransactionDate(new Date());

		giftDeedEntity.setDate(gdEntity.getDate());
		giftDeedEntity.setDonorAccountName(gdEntity.getDonorAccountName());
		giftDeedEntity.setDonorAccountNumber(gdEntity.getDonorAccountNumber());
		giftDeedEntity.setDonorAddress(gdEntity.getDonorAddress());
		giftDeedEntity.setDonorName(gdEntity.getDonorName());
		giftDeedEntity.setRelationshipToDonee(gdEntity.getRelationshipToDonee());
		giftDeedEntity.setDonorBankName(gdEntity.getDonorBankName());
		giftDeedEntity.setTransferMode(gdEntity.getTransferMode());

		giftDeedEntity.setAmountGifted(gdEntity.getAmountGifted());
		giftDeedEntity.setDoneeAccountName(gdEntity.getDoneeAccountName());
		giftDeedEntity.setDoneeAccountNumber(gdEntity.getDoneeAccountNumber());
		giftDeedEntity.setDoneeAddress(gdEntity.getDoneeAddress());
		giftDeedEntity.setDoneeName(gdEntity.getDoneeName());
		giftDeedEntity.setDoneeBankName(gdEntity.getDoneeBankName());
		giftDeedEntity.setRelationshipToDonor(gdEntity.getRelationshipToDonor());

		giftDeedEntity.setFirstWitnessAddress(gdEntity.getFirstWitnessAddress());
		giftDeedEntity.setFirstWitnessName(gdEntity.getFirstWitnessName());
		giftDeedEntity.setFirstWitnessOccupation(gdEntity.getFirstWitnessOccupation());

		giftDeedEntity.setSecondtWitnessName(gdEntity.getSecondWitnessName());
		giftDeedEntity.setSecondWitnessAddress(gdEntity.getSecondWitnessAddress());
		giftDeedEntity.setSecondWitnessOccupation(gdEntity.getSecondWitnessOccupation());

		giftDeedEntity.setDenomination(gdEntity.getDenomination());

		giftDeedEntity.setStatus("Pending Payment");
		giftDeedEntity.setReference(referenceNumber);

		// get the service cost from the user type..
		UserEntity userEntity = userDao.getById(user_id);
		String userType = userEntity.getUserType();
		ServiceCostEntity SCE = serviceRepo.findByUserType(userType);

		serviceCost = SCE.getGiftDeed();

		giftDeedEntity.setUserEntity(userEntity);

		giftRepo.save(giftDeedEntity);
		gd_id = giftDeedEntity.getId();

		InitializeTransactionRequest request = new InitializeTransactionRequest();

		InitializeTransactionResponse initializeTransactionResponse = null;

		try{

			request.setReference(referenceNumber);
			request.setCallback_url(base_url + "/v1/service/giftdeed/savetransaction/" +request.getReference());
			request.setAmount(serviceCost * 100);
			request.setEmail(currentUser.getUsername());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer "+secretKey);

			HttpEntity<InitializeTransactionRequest> httpEntity = new HttpEntity<>(request, headers);

			ResponseEntity<?> responseEntity =
					restTemplate.exchange("https://api.paystack.co/transaction/initialize", HttpMethod.POST, httpEntity, InitializeTransactionResponse.class);

			initializeTransactionResponse  = (InitializeTransactionResponse) responseEntity.getBody();

		}catch (Exception exception){
			log.error("Error initializing transaction: {}"+exception.getLocalizedMessage());
			throw new RuntimeException("Error initializing transaction: {}"+exception.getLocalizedMessage());
		}

		return initializeTransactionResponse;

	}

	// verify to save..

	public PaymentEntity verifyTransactionToSave(String reference) throws Exception {

		VerifyTransactionResponse paystackresponse;

		PaymentEntity paymentEntity = null;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet("https://api.paystack.co/transaction/verify/" + reference);

		request.addHeader("Content-type", "application/json");
		request.addHeader("Authorization", "Bearer " + secretKey);

		StringBuilder result = new StringBuilder();
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

		} else {
			throw new Exception("Error Occured while connecting to paystack url");
		}

		ObjectMapper mapper = new ObjectMapper();

		paystackresponse = mapper.readValue(result.toString(), VerifyTransactionResponse.class);

		if (paystackresponse == null || paystackresponse.getStatus().equals("false")) {
			throw new Exception("An error occurred while  verifying payment");
		} else if (paystackresponse.getData().getStatus().equals("success")) {
			System.out.println("verification complete!!!");

			data = paystackresponse.getData();

			if (data.getStatus().equals("success")) {
				
				if(giftRepo.existsByReference(reference)) {
					
					GiftDeedEntity giftEntity = giftRepo.findByReference(reference);
					
					UserEntity userEntity = giftEntity.getUserEntity();
					
					paymentEntity = new PaymentEntity();

					paymentEntity.setService("Financial Gift Deed");
					paymentEntity.setDate(new Date());
					paymentEntity.setReference(data.getReference());
					paymentEntity.setStatus("Awaiting Video Verification");

					paymentEntity.setUserEntity(userEntity);

					paymentRepo.save(paymentEntity);

					updateAfterPayment(data.getReference());
					
					
				}else {
					throw new ReferenceNotFound();
				}

			}
		}
		return paymentEntity;
	}

	// update payment data..

	public void updateAfterPayment(String reference) {
		
		GiftDeedEntity giftDeedEntity = giftRepo.findByReference(reference);
		
		UserEntity userEntity = giftDeedEntity.getUserEntity();

		String userEmail = userEntity.getUsername();
		String fullname = userEntity.getFirstname() + " " + userEntity.getLastname();


		giftDeedEntity.setStatus("Awaiting Video Verification");
		giftDeedEntity.setReference(reference);
		giftDeedEntity.setUserEntity(userEntity);

		giftRepo.save(giftDeedEntity);

		GiftDeedEntity giftDeedEntity1 = giftRepo.getById(gd_id);

		try {
			createPDF(tmpDirsLocationD + reference + ".pdf", giftDeedEntity1, notaryName);
			log.info("Document created");

			log.info("attempting to upload gift deed document to S3");

			// first document..
			File newFile = new File(tmpDirsLocationD + reference + ".pdf");
			awsService.uploadFile(newFile);

			log.info("Document uploaded");

		} catch (Exception nn) {
			log.error("Error generating document " + nn);
			nn.printStackTrace();
		}

		try {

			sendEmail.sendVerificationMail("Financial Gift Deed", giftDeedEntity.getReference(), fullname, userEmail);

		} catch (Exception nn) {
			log.error("error sending verification email " + nn.getMessage());
		}

	}

	@Async
	public void createPDF(String filename, GiftDeedEntity giftDeedEntity, String notaryName) throws Exception {

		giftDeedPDF = new GiftDeedPDF();

		giftDeedPDF.getPDFDocument(filename, giftDeedEntity, notaryName);

	}

	// find gift deed by reference..

	public GiftDeedEntity getByReference(String reference) {

		return giftRepo.findByReference(reference);
	}

}
