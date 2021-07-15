package com.backend.notariza.service.custom;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.custom.InitPayment;
import com.backend.notariza.paystack.Data;
import com.backend.notariza.paystack.VerifyTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InitPaymentServiceImpl implements InitPaymentService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	InitPayment initPayment;

	Data data;

	private static final int STATUS_CODE_OK = 200;
	@Value("${paystack.testkey}")
	private String secretKey;

	@Value("${base_url}")
	private String base_url;

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

			if (data.getStatus().equals("success")) {

				log.info("PAYMENT CONFIRMED ==>" + data.getReference());

			} else {
				log.error("PAYMENT DECLINED ==>" + data.getReference());
			}
		}

		return paystackresponse;
	}

}
