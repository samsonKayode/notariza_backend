package com.backend.notariza.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.NotarizeDocumentRepo;
import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.dao.ServiceCostRepo;
import com.backend.notariza.dao.UserDao;
import com.backend.notariza.dto.NotarizeDocumentDTO;
import com.backend.notariza.entity.NotarizeDocumentEntity;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.ServiceCostEntity;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.exceptions.ReferenceNotFound;
import com.backend.notariza.paystack.Data;
import com.backend.notariza.paystack.InitializeTransactionRequest;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.paystack.VerifyTransactionResponse;
import com.backend.notariza.service.custom.AWSS3Service;
import com.backend.notariza.util.NotarizeDocumentUtil;
import com.backend.notariza.util.RandomReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

@Service
public class NotarizeDocumentService {

	@Value("${notary_name}")
	private String notaryName;

	@Autowired
	NotarizeDocumentRepo notarizeRepo;

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

	@Autowired
	PaymentRepository paymentRepo;

	@Autowired
	ServiceCostRepo serviceRepo;

	int serviceCost = 0;

	NotarizeDocumentEntity notarizeDocumentEntity;

	int nd_id = 0;

	Data data;

	int pages;

	String filePath;

	NotarizeDocumentUtil notarizeDocumentItil = new NotarizeDocumentUtil();

	public String tmpDir = System.getProperty("java.io.tmpdir") + "/";

	@Autowired
	private AWSS3Service awsService;

	File file3;

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SendEmailService sendEmail;

	@Autowired
	RestTemplate restTemplate;
	
	String referenceNumber=null;

	// make payment..

	public InitializeTransactionResponse makePayment(NotarizeDocumentDTO notarizeEntity, int user_id) throws Exception {

		notarizeDocumentEntity = new NotarizeDocumentEntity();
		
		referenceNumber=randomReference.getAlphaNumericString(40);

		notarizeDocumentEntity.setAgent_code(notarizeEntity.getAgent_code());
		notarizeDocumentEntity.setTitle(notarizeEntity.getTitle());
		notarizeDocumentEntity.setDate(new Date());
		notarizeDocumentEntity.setStatus("Pending Payment");
		notarizeDocumentEntity.setReference(referenceNumber);

		UserEntity userEntity = userDao.getById(user_id);
		String userType = userEntity.getUserType();
		ServiceCostEntity SCE = serviceRepo.findByUserType(userType);
		serviceCost = SCE.getNotarizeDocument();

		notarizeDocumentEntity.setUserEntity(userEntity);

		notarizeDocumentEntity.setFile(notarizeEntity.getFile());

		notarizeDocumentEntity.setCtc(notarizeEntity.isCtc());

		RandomReference random = new RandomReference();


		Resource mpf = notarizeDocumentEntity.getFile().getResource();

		file3 = new File(tmpDir + random.getAlphaNumericString(20) + ".pdf");
		Files.copy(mpf.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

		boolean f3E = file3.exists();

		if (f3E) {
			log.info("FILE 3 EXISTS");
			log.info("FILE 3====>" + file3.getAbsolutePath());

			filePath = file3.getAbsolutePath();

			pages = notarizeDocumentItil.numberOfPages(new File(file3.getAbsolutePath()));
		} else {
			log.info("FILE 3 DOES NOT");
		}

		notarizeRepo.save(notarizeDocumentEntity);

		nd_id = notarizeDocumentEntity.getId();

		InitializeTransactionRequest request = new InitializeTransactionRequest();

		InitializeTransactionResponse initializeTransactionResponse = null;

		try{

			request.setReference(referenceNumber);
			request.setCallback_url(base_url + "/v1/service/notarizedocument/savetransaction/" + request.getReference());
			request.setAmount(pages * serviceCost * 100);
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

	// verify and save transactions..

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
				
				if(notarizeRepo.existsByReference(reference)) {
					
					NotarizeDocumentEntity notarizeEntity = notarizeRepo.findByReference(reference);
					
					UserEntity userEntity = notarizeEntity.getUserEntity();
						
						paymentEntity = new PaymentEntity();

						paymentEntity.setService("Notarize Document");
						paymentEntity.setDate(new Date());
						paymentEntity.setReference(data.getReference());
						paymentEntity.setUserEntity(userEntity);
						paymentEntity.setStatus("Awaiting Video Verification");

						paymentRepo.save(paymentEntity);

						updateAfterPayment(data.getReference());

				}else {
					
					throw new ReferenceNotFound();
				}

			}
		}

		return paymentEntity;
	}

	// update status to awaiting video, generate pdf and save...

	@Async
	public void updateAfterPayment(String reference) throws Exception {
		
		NotarizeDocumentEntity nDE = notarizeRepo.findByReference(reference);

		UserEntity userEntity = nDE.getUserEntity();

		String userEmail = userEntity.getUsername();
		String fullname = userEntity.getFirstname() + " " + userEntity.getLastname();

		boolean ctc = nDE.isCtc();

		nDE.setReference(reference);
		nDE.setFileContent(null);
		nDE.setStatus("Awaiting Video Verification");
		nDE.setUserEntity(userEntity);
		notarizeRepo.save(nDE);

		// Notarize the document....
		notarizeDocumentItil.loadDocument(new File(filePath), tmpDir + reference + ".pdf", ctc);

		// first document..
		File newFile = new File(tmpDir + reference + ".pdf");
		awsService.uploadFile(newFile);

		// delete file3...

		String fpath = file3.getAbsolutePath();
		log.info("deleting " + fpath);

		file3.delete();
		log.info("temp file " + fpath + "deleted");

		// send verificationEmail..
		try {

			sendEmail.sendVerificationMail(nDE.getTitle(), nDE.getReference(), fullname, userEmail);

		} catch (Exception nn) {
			log.error("error sending verification email " + nn.getMessage());
		}

	}

	// find by reference..
	public NotarizeDocumentEntity getByReference(String reference) {

		return notarizeRepo.findByReference(reference);
	}

}
