package com.backend.notariza.paystack;

import com.backend.notariza.pojos.PayStackPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * This class represents the post object that would initialize a paystack transaction
 * to generate the url that would be used for payment.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitializeTransactionRequest {

    /**
     Amount in kobo
     */
    private Integer amount;

    /**
     * The email of the user performing this transaction
     * this if often used as a unique identifier for this customer
     * an email would be sent to this customer after this transaction
     */
    private String email;

    /**
     * This is a unique identifier that help paystack identify this transaction.
     *
     * Never send the same reference twice.
     * Only send Alphanumeric characters and -, . and =. Other characters are invalid (no spaces, newlines etc).
     */
    private String reference;


    /**
     * Fully qualified url, e.g. https://nut-ng.org/ .
     * Use this to override the callback url provided on the dashboard for this transaction
     */
    private String callback_url;

    /**
     * Used to apply a multiple to the amount returned by the plan code above.
     */
    private Float quantity;

    /**
     * Number of invoices to raise during the subscription.
     * Overrides invoice_limit set on plan.
     */
    private Integer invoice_limit;

    /**
     * Extra information to be saved with this transaction
     */
    private MetaData metadata;
    
    private PayStackPojo payStackPojo;

    /**
     * A flat fee to charge the subaccount for this transaction, in kobo.
     * This overrides the split percentage set when the subaccount was created.
     * Ideally, you will need to use this if you are splitting in flat rates
     * (since subaccount creation only allows for percentage split). e.g. 7000 for a 70 naira flat fee.
     */
    
    private Integer transaction_charge;
    
    public InitializeTransactionRequest() {
    	
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    
    public MetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }
    

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Integer getInvoice_limit() {
        return invoice_limit;
    }

    public void setInvoice_limit(Integer invoice_limit) {
        this.invoice_limit = invoice_limit;
    }



    public Integer getTransaction_charge() {
        return transaction_charge;
    }

    public void setTransaction_charge(Integer transaction_charge) {
        this.transaction_charge = transaction_charge;
    }

	public PayStackPojo getPayStackPojo() {
		return payStackPojo;
	}

	public void setPayStackPojo(PayStackPojo payStackPojo) {
		this.payStackPojo = payStackPojo;
	}
    
    

}
