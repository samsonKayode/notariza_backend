package com.backend.notariza.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "financial_gift_deed")
public class GiftDeedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Date transactionDate;
	
	@PastOrPresent(message="the date cannot be greater than the current date")
	private Date date;

	@NotBlank(message = "donoor name is mandatory")
	@Size(min=4)
	private String donorName;

	@NotBlank(message = "donor address is mandatory")
	@Size(min=5)
	private String donorAddress;

	@NotBlank(message = "relationship to donee is mandatory")
	@Size(min=4)
	private String relationshipToDonee;

	@Min(value=100, message="the amount must be greater than 100")
	@Positive(message="you cannot enter a negative figure")
	private float amountGifted;

	@NotBlank(message = "donor bank name is mandatory")
	@Size(min=3)
	private String donorBankName;

	@NotBlank(message = "donor account number is mandatory")
	@Size(min=5)
	private String donorAccountNumber;

	@NotBlank(message = "donor account name is mandatory")
	@Size(min=5)
	private String donorAccountName;

	@NotBlank(message = "transfer mode is mandatory")
	@Size(min=4)
	private String transferMode;

	@NotBlank(message = "donee name is mandatory")
	@Size(min=5)
	private String doneeName;

	@NotBlank(message = "donee address is mandatory")
	@Size(min=5)
	private String doneeAddress;

	@NotBlank(message = "relationship to donor is mandatory")
	@Size(min=4)
	private String relationshipToDonor;

	@NotBlank(message = "donee bank name is mandatory")
	@Size(min=3)
	private String doneeBankName;

	@NotBlank(message = "donee account number is mandatory")
	@Size(min=5)
	private String doneeAccountNumber;

	@NotBlank(message = "donee account name is mandatory")
	@Size(min=4)
	private String doneeAccountName;
	
	@NotBlank(message = "donor witness name is mandatory")
	@Size(min=4)
	private String firstWitnessName;
	
	@NotBlank(message = "donor witness address is mandatory")
	@Size(min=5)
	private String firstWitnessAddress;
	
	@NotBlank(message = "donor witness occupation is mandatory")
	@Size(min=4)
	private String firstWitnessOccupation;
	
	@NotBlank(message = "donee witness name is mandatory")
	@Size(min=4)
	private String secondWitnessName;
	
	@NotBlank(message = "donee witness address is mandatory")
	@Size(min=5)
	private String secondWitnessAddress;
	
	@NotBlank(message = "donee witness occupation is mandatory")
	@Size(min=5)
	private String secondWitnessOccupation;
	
	@NotBlank(message = "denomination is mandatory")
	@Size(max=1)
	private String denomination;
	
	private String status;
	
	private String reference;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable=false)
	private UserEntity userEntity;
	
	public GiftDeedEntity() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDonorName() {
		return donorName;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}

	public String getDonorAddress() {
		return donorAddress;
	}

	public void setDonorAddress(String donorAddress) {
		this.donorAddress = donorAddress;
	}

	public String getRelationshipToDonee() {
		return relationshipToDonee;
	}

	public void setRelationshipToDonee(String relationshipToDonee) {
		this.relationshipToDonee = relationshipToDonee;
	}

	public float getAmountGifted() {
		return amountGifted;
	}

	public void setAmountGifted(float amountGifted) {
		this.amountGifted = amountGifted;
	}

	public String getDonorBankName() {
		return donorBankName;
	}

	public void setDonorBankName(String donorBankName) {
		this.donorBankName = donorBankName;
	}

	public String getDonorAccountNumber() {
		return donorAccountNumber;
	}

	public void setDonorAccountNumber(String donorAccountNumber) {
		this.donorAccountNumber = donorAccountNumber;
	}

	public String getDonorAccountName() {
		return donorAccountName;
	}

	public void setDonorAccountName(String donorAccountName) {
		this.donorAccountName = donorAccountName;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getDoneeName() {
		return doneeName;
	}

	public void setDoneeName(String doneeName) {
		this.doneeName = doneeName;
	}

	public String getDoneeAddress() {
		return doneeAddress;
	}

	public void setDoneeAddress(String doneeAddress) {
		this.doneeAddress = doneeAddress;
	}

	public String getRelationshipToDonor() {
		return relationshipToDonor;
	}

	public void setRelationshipToDonor(String relationshipToDonor) {
		this.relationshipToDonor = relationshipToDonor;
	}

	public String getDoneeBankName() {
		return doneeBankName;
	}

	public void setDoneeBankName(String doneeBankName) {
		this.doneeBankName = doneeBankName;
	}

	public String getDoneeAccountNumber() {
		return doneeAccountNumber;
	}

	public void setDoneeAccountNumber(String doneeAccountNumber) {
		this.doneeAccountNumber = doneeAccountNumber;
	}

	public String getDoneeAccountName() {
		return doneeAccountName;
	}

	public void setDoneeAccountName(String doneeAccountName) {
		this.doneeAccountName = doneeAccountName;
	}

	public String getFirstWitnessName() {
		return firstWitnessName;
	}

	public void setFirstWitnessName(String firstWitnessName) {
		this.firstWitnessName = firstWitnessName;
	}

	public String getFirstWitnessAddress() {
		return firstWitnessAddress;
	}

	public void setFirstWitnessAddress(String firstWitnessAddress) {
		this.firstWitnessAddress = firstWitnessAddress;
	}

	public String getFirstWitnessOccupation() {
		return firstWitnessOccupation;
	}

	public void setFirstWitnessOccupation(String firstWitnessOccupation) {
		this.firstWitnessOccupation = firstWitnessOccupation;
	}

	public String getSecondWitnessName() {
		return secondWitnessName;
	}

	public void setSecondtWitnessName(String secondWitnessName) {
		this.secondWitnessName = secondWitnessName;
	}

	public String getSecondWitnessAddress() {
		return secondWitnessAddress;
	}

	public void setSecondWitnessAddress(String secondWitnessAddress) {
		this.secondWitnessAddress = secondWitnessAddress;
	}

	public String getSecondWitnessOccupation() {
		return secondWitnessOccupation;
	}

	public void setSecondWitnessOccupation(String secondWitnessOccupation) {
		this.secondWitnessOccupation = secondWitnessOccupation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	
	
}
