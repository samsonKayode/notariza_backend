package com.backend.notariza.dao.custom;


import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.paystack.InitializeTransactionRequest;
import com.backend.notariza.paystack.InitializeTransactionResponse;
import com.backend.notariza.paystack.VerifyTransactionResponse;


public interface InitPayment {
	
	public InitializeTransactionResponse startPayment(InitializeTransactionRequest request, int user_id);
	
	public VerifyTransactionResponse verifyTransaction(String reference) throws Exception;

	public PaymentEntity savePaystackRecord(PaymentEntity paymentEntity, int user_id);
	
	//public VerifyTransactionResponse verifyTransactionToSave(String reference, int user_id) throws Exception;

}
