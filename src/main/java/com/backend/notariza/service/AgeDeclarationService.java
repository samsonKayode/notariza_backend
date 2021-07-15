package com.backend.notariza.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

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
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.AgeDeclarationRepo;
import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.dao.ServiceCostRepo;
import com.backend.notariza.dao.UserDao;
import com.backend.notariza.entity.AgeDeclarationEntity;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.ServiceCostEntity;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.exceptions.ReferenceNotFound;
import com.backend.notariza.paystack.Data;
import com.backend.notariza.paystack.InitializeTransactionRequest;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.paystack.VerifyTransactionResponse;
import com.backend.notariza.service.custom.AWSS3Service;
import com.backend.notariza.util.AgeDeclarationPDF;
import com.backend.notariza.util.RandomReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service
public class AgeDeclarationService {

	@Value("${notary_name}")
	private String notaryName;

	@Autowired
	private AWSS3Service awsService;

	@Autowired
	UserDao userDao;

	@Autowired
	AgeDeclarationRepo ageDeclarationRepo;

	@Autowired
	CurrentUser currentUser;

	RandomReference randomReference = new RandomReference();

	private static final int STATUS_CODE_OK = 200;
	@Value("${paystack.testkey}")
	private String secretKey;

	@Value("${base_url}")
	private String base_url;

	AgeDeclarationEntity ageDeclaration;

	Data data;

	@Autowired
	PaymentRepository paymentRepo;

	int age_id = 0;

	int serviceCost = 0;

	@Autowired
	ServiceCostRepo serviceRepo;

	@Autowired
	SendEmailService sendEmailService;

	AgeDeclarationPDF generatePDF = new AgeDeclarationPDF();

	public String tmpDirsLocation = System.getProperty("java.io.tmpdir") + "/";

	Logger log = LoggerFactory.getLogger(this.getClass());

	String referenceNumber = null;

	// make payment..

	public InitializeTransactionResponse makePayment(AgeDeclarationEntity ageDeclarationEntity, int user_id) {

		// Save ageDeclaration data..

		ageDeclaration = new AgeDeclarationEntity();

		referenceNumber = randomReference.getAlphaNumericString(40);

		ageDeclaration.setAddress(ageDeclarationEntity.getAddress());
		ageDeclaration.setDob(ageDeclarationEntity.getDob());
		ageDeclaration.setFirstname(ageDeclarationEntity.getFirstname());
		ageDeclaration.setLastname(ageDeclarationEntity.getLastname());
		ageDeclaration.setSex(ageDeclarationEntity.getSex());
		ageDeclaration.setPlaceOfBirth(ageDeclarationEntity.getPlaceOfBirth());

		ageDeclaration.setReason(ageDeclarationEntity.getReason());

		ageDeclaration.setOwner(ageDeclarationEntity.isOwner());

		if (!ageDeclarationEntity.isOwner()) {

			ageDeclaration.setOwnerSex(ageDeclarationEntity.getOwnerSex());
			ageDeclaration.setOwnerName(ageDeclarationEntity.getOwnerName());
			ageDeclaration.setRelationshipToOwner(ageDeclarationEntity.getRelationshipToOwner());
		}

		ageDeclaration.setStatus("Pending Payment");
		ageDeclaration.setReference(referenceNumber);
		ageDeclaration.setDate(new Date());

		UserEntity userEntity = userDao.getById(user_id);
		ageDeclaration.setUserEntity(userEntity);

		String userType = userEntity.getUserType();

		ServiceCostEntity SCE = serviceRepo.findByUserType(userType);
		serviceCost = SCE.getAgeDeclaration();

		ageDeclarationRepo.save(ageDeclaration);

		age_id = ageDeclaration.getId();

		InitializeTransactionRequest request = new InitializeTransactionRequest();

		InitializeTransactionResponse initializeTransactionResponse = null;

		try {
			// convert transaction to json then use it as a body to post json
			Gson gson = new Gson();
			// add paystack chrges to the amoun

			request.setReference(referenceNumber);

			request.setCallback_url(base_url + "/v1/service/ageDeclaration/savetransaction/" + request.getReference());

			request.setAmount(serviceCost * 100);

			request.setEmail(currentUser.getUsername());

			StringEntity postingString = new StringEntity(gson.toJson(request));
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost("https://api.paystack.co/transaction/initialize");

			post.setEntity(postingString);
			post.addHeader("Content-type", "application/json");
			post.addHeader("Authorization", "Bearer " + secretKey);
			StringBuilder result = new StringBuilder();
			HttpResponse response = client.execute(post);

			int STATUSR = response.getStatusLine().getStatusCode();

			if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

			} else {
				throw new Exception(
						"Error Occurred while initializing transaction : STATUS RESPONSE ====>>>" + STATUSR);
			}
			ObjectMapper mapper = new ObjectMapper();

			initializeTransactionResponse = mapper.readValue(result.toString(), InitializeTransactionResponse.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			// throw new Exception("Failure initializaing paystack transaction");
		}

		return initializeTransactionResponse;
	}

	// Callback URL..

	public PaymentEntity callBackURL(String reference) throws Exception {
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

				if (ageDeclarationRepo.existsByReference(reference)) {

					AgeDeclarationEntity ageDeclarationEntity = ageDeclarationRepo.findByReference(reference);

					UserEntity userEntity = ageDeclarationEntity.getUserEntity();

					String userEmail = userEntity.getUsername();
					String fullname = userEntity.getFirstname() + " " + userEntity.getLastname();

					paymentEntity = new PaymentEntity();

					paymentEntity.setService("Age Declaration");
					paymentEntity.setDate(new Date());
					paymentEntity.setReference(data.getReference());
					paymentEntity.setStatus("CONFIRMED");

					paymentEntity.setUserEntity(userEntity);

					paymentRepo.save(paymentEntity);

					updateAfterPayment(data.getReference());

					// send email..

					sendEmailService.sendMail("Age Declaration", fullname, userEmail);

				}

				else {
					throw new ReferenceNotFound();
				}

			}
		}

		return paymentEntity;
	}

	// update age declaration entity..

	public void updateAfterPayment(String reference) {

		AgeDeclarationEntity ageDeclarationEntity = ageDeclarationRepo.findByReference(reference);

		UserEntity userEntity = ageDeclarationEntity.getUserEntity();

		ageDeclarationEntity.setReference(reference);
		ageDeclarationEntity.setStatus("Paid");
		ageDeclarationEntity.setUserEntity(userEntity);

		ageDeclarationRepo.save(ageDeclarationEntity);

		try {
			generatePDF.getPDFDocument(tmpDirsLocation + reference, ageDeclarationEntity, notaryName);

			File newFile = new File(tmpDirsLocation + reference + ".pdf");
			awsService.uploadFile(newFile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Get age declaration by reference...

	public AgeDeclarationEntity getByReference(String reference) {

		return ageDeclarationRepo.findByReference(reference);
	}
}
