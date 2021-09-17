package com.backend.notariza.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

import com.backend.notariza.util.DocumentResources;
import com.backend.notariza.util.DocumentResourcesListPojo;
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
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.ChangeOfNameRepo;
import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.dao.ServiceCostRepo;
import com.backend.notariza.dao.UserDao;
import com.backend.notariza.entity.ChangeOfNameEntity;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.ServiceCostEntity;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.exceptions.ReferenceNotFound;
import com.backend.notariza.paystack.Data;
import com.backend.notariza.paystack.InitializeTransactionRequest;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.paystack.VerifyTransactionResponse;
import com.backend.notariza.service.custom.AWSS3Service;
import com.backend.notariza.util.ChangeOfNamePDF;
import com.backend.notariza.util.RandomReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

@Service
public class ChangeOfNameService {

	@Value("${notary_name}")
	private String notaryName;

	@Autowired
	private AWSS3Service awsService;

	@Autowired
	private ChangeOfNameRepo changeOfNameRepo;

	@Autowired
	private UserDao userDao;

	@Autowired
	DocumentResources documentResources;

	DocumentResourcesListPojo documentResourcesListPojo;

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

	@Autowired
	SendEmailService sendEmailService;

	@Autowired
	RestTemplate restTemplate;

	int serviceCost = 0;

	ChangeOfNameEntity coe;

	ChangeOfNamePDF generatePDF = new ChangeOfNamePDF();

	int cone_id = 0;

	String referenceNumber = null;

	String tmpDirsLocation = System.getProperty("java.io.tmpdir") + "/";

	Logger log = LoggerFactory.getLogger(this.getClass());

	// Update status and reference..

	public ChangeOfNameEntity updateData(int id, String reference) {

		ChangeOfNameEntity coe = changeOfNameRepo.getById(id);

		coe.setReference(reference);
		coe.setStatus("Paid");

		return changeOfNameRepo.save(coe);
	}

	// Make payment with Paystack...
	public InitializeTransactionResponse makePayment(ChangeOfNameEntity changeOfNameEntity, int user_id) {

		coe = new ChangeOfNameEntity();

		referenceNumber = randomReference.getAlphaNumericString(40);

		coe.setAddress(changeOfNameEntity.getAddress());
		coe.setSex(changeOfNameEntity.getSex());
		coe.setDob(changeOfNameEntity.getDob());
		coe.setFirstname(changeOfNameEntity.getFirstname());
		coe.setSurname(changeOfNameEntity.getSurname());
		coe.setFormer_name(changeOfNameEntity.getFormer_name());
		coe.setStatus("Pending Payment");
		coe.setDate(new Date());
		coe.setReference(referenceNumber);

		UserEntity userEntity = userDao.getById(user_id);
		String userType = userEntity.getUserType();
		ServiceCostEntity SCE = serviceRepo.findByUserType(userType);
		serviceCost = SCE.getChangeOfName();

		coe.setUserEntity(userEntity);

		changeOfNameRepo.save(coe);
		cone_id = coe.getId();

		log.info("REFERENCE NUMBER===>>>" + referenceNumber);

		InitializeTransactionRequest request = new InitializeTransactionRequest();

		InitializeTransactionResponse initializeTransactionResponse = null;

		try{

			request.setReference(referenceNumber);
			request.setCallback_url(base_url + "/v1/service/changeofname/savetransaction/" + request.getReference());
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

		/*

		try {
			// convert transaction to json then use it as a body to post json
			Gson gson = new Gson();
			// add paystack chrges to the amount

			request.setReference(referenceNumber);
			request.setCallback_url(base_url + "/v1/service/changeofname/savetransaction/" + request.getReference());

			request.setAmount(serviceCost * 100);

			request.setEmail(currentUser.getUsername());

			StringEntity postingString = new StringEntity(gson.toJson(request));
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost("https://api.paystack.co/transaction/initialize");

			post.setEntity(postingString);
			post.addHeader("Content-type", "application/json");
			post.addHeader("Authorization", "Bearer " + secretKey);
			post.addHeader("Cache-Control", "no-cache");

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

		 */

		return initializeTransactionResponse;
	}

	// verify and save transaction..

	public PaymentEntity verifyTransactionToSave(String reference) throws Exception {

		VerifyTransactionResponse paystackresponse;
		PaymentEntity paymentEntity = null;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet("https://api.paystack.co/transaction/verify/" + reference);

		request.addHeader("Content-type", "application/json");
		request.addHeader("Authorization", "Bearer " + secretKey);
		request.addHeader("Cache-Control", "no-cache");

		StringBuilder result = new StringBuilder();
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

		} else {
			log.error("Error verifying paystack transaction.. STATUS CODE===>"
					+ response.getStatusLine().getStatusCode());
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
				// verify reference..

				if (changeOfNameRepo.existsByReference(reference)) {

					ChangeOfNameEntity changeOfNameEntity = changeOfNameRepo.findByReference(reference);

					UserEntity userEntity = changeOfNameEntity.getUserEntity();

					String userEmail = userEntity.getUsername();
					String fullname = userEntity.getFirstname() + " " + userEntity.getLastname();

					System.out.println("FULLNAME====>>>>" + fullname);

					paymentEntity = new PaymentEntity();

					paymentEntity.setService("Change Of Name");
					paymentEntity.setDate(new Date());
					paymentEntity.setReference(data.getReference());
					paymentEntity.setStatus("APPROVED");

					paymentEntity.setUserEntity(userEntity);

					paymentRepo.save(paymentEntity);

					updateAfterPayment(data.getReference());
					// Send email

					sendEmailService.sendMail("Change Of Name", fullname, userEmail);

				} else {

					throw new ReferenceNotFound();
				}

			} else {
				log.error(" ===>> failed");
			}
		}

		return paymentEntity;
	}

	public void updateAfterPayment(String reference) {

		try{
			documentResourcesListPojo = documentResources.getData();
			log.info("bytes gotten");
		}catch(Exception e){
			log.error("COULD NOT GET BYTES==="+e.getLocalizedMessage());
		}

		ChangeOfNameEntity cone = changeOfNameRepo.findByReference(reference);

		UserEntity userEntity = cone.getUserEntity();

		// ChangeOfNameEntity cone = changeOfNameRepo.getById(cone_id);

		cone.setReference(reference);
		cone.setStatus("Paid");
		cone.setUserEntity(userEntity);

		changeOfNameRepo.save(cone);

		String newName = cone.getSurname() + " " + cone.getFirstname();

		try {

			System.out.println("Filename2 ChangeOfNameService=========>" + tmpDirsLocation + reference);

			generatePDF.getPDFDocument(tmpDirsLocation + reference, cone.getFormer_name(), newName, cone.getAddress(),
					cone.getSex(), notaryName, documentResourcesListPojo);

			log.info("File " + reference + ".pdf generated");
			log.info("File saved to temp folder " + tmpDirsLocation);

			File newFile = new File(tmpDirsLocation + reference + ".pdf");

			log.info("File upload in progress");

			awsService.uploadFile(newFile);

			log.info("File Uploaded");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// get change of name data by reference...

	public ChangeOfNameEntity getOne(String reference) {

		return changeOfNameRepo.findByReference(reference);
	}

}
