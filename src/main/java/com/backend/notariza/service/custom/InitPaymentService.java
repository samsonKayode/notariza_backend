package com.backend.notariza.service.custom;

import com.backend.notariza.paystack.VerifyTransactionResponse;

public interface InitPaymentService {
	
	
	public VerifyTransactionResponse verifyTransaction(String reference) throws Exception;

	
	//public VerifyTransactionResponse verifyTransactionToSave(String reference, int user_id) throws Exception;


}
