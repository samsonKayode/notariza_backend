package com.backend.notariza.dao.custom;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.dao.UserDao;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.paystack.Data;
import com.backend.notariza.paystack.InitializeTransactionRequest;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.paystack.MetaData;
import com.backend.notariza.paystack.VerifyTransactionResponse;
import com.backend.notariza.util.RandomReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


@Repository
public class InitPaymentImpl implements InitPayment {
	
	private static final int STATUS_CODE_OK = 200;
	@Value("${paystack.testkey}")
	private String secretKey;
	
	@Value("${base_url}")
	private String base_url;
	
	Data data;
	
	MetaData metaData;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	PaymentRepository paymentRepo;
	
	@Autowired
	UserDao userDao;
	
	PaymentEntity paymentEntity = new PaymentEntity();
	
	RandomReference randomReference = new RandomReference();
	
	String customerEmail=null;
	
	String titleID=null;

	@Override
	public InitializeTransactionResponse startPayment(InitializeTransactionRequest request,  int user_id) {
		
		UserEntity userEntity = userDao.getById(user_id);
		
		InitializeTransactionResponse initializeTransactionResponse = null;
		
		try {
            // convert transaction to json then use it as a body to post json
            Gson gson = new Gson();
            // add paystack chrges to the amoun
            
            request.setReference(randomReference.getAlphaNumericString(40));
            request.setCallback_url(base_url+"/v1/payment/savetransaction/"+request.getReference()+"/"+user_id);
            
            
            request.setAmount(request.getAmount());
            
            request.setEmail(userEntity.getUsername());
            
            StringEntity postingString = new StringEntity(gson.toJson(request));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("https://api.paystack.co/transaction/initialize");
            
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer "+secretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);
            
            customerEmail = request.getEmail();
            
            titleID = request.getMetadata().getService();
            
            int STATUSR = response.getStatusLine().getStatusCode();
            
            if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

            } else {
                throw new Exception("Error Occurred while initializing transaction : STATUS RESPONSE ====>>>"+STATUSR);
            }
            ObjectMapper mapper = new ObjectMapper();

            initializeTransactionResponse = mapper.readValue(result.toString(), InitializeTransactionResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw new Exception("Failure initializaing paystack transaction");
        }

		return initializeTransactionResponse;
	}
	
	@Override
	public VerifyTransactionResponse verifyTransaction(String reference) throws Exception {
		
		VerifyTransactionResponse paystackresponse;
	
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
           
           if(data.getGateway_response().equals("Successful")) { 
        	   
        	   System.out.println("Payment Successful");

           }
        }

		//System.out.println("PAYMENT STATUS::====>"+ data.getGateway_response());
		
        return paystackresponse;
	}
	
	//save successful payment to database..
	
	@Override
	@Transactional
	public PaymentEntity savePaystackRecord(PaymentEntity payEntity, int user_id) {
		
		UserEntity userEntity = userDao.getById(user_id);
		
		PaymentEntity paymentEntity = new PaymentEntity();
		
		paymentEntity.setService(payEntity.getService());
		paymentEntity.setDate(payEntity.getDate());
		paymentEntity.setReference(payEntity.getReference());
		
		paymentEntity.setUserEntity(userEntity);
		
		return paymentRepo.save(paymentEntity);
		
	}

	
	
	
	
}
