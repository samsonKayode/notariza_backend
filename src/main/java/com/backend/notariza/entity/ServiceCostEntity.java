package com.backend.notariza.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="service_cost_table")
public class ServiceCostEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message="user type is mandatory")
	@Size(min=4)
	@Column(name="user_type", unique=true)
	private String userType;
	
	@Column(name="missing_item")
	private int missingItem;
	
	@Min(value=100, message="requires 100 minimum")
	@Column(name="change_of_name")
	private int changeOfName;
	
	@Min(value=100, message="requires 100 minimum")
	@Column(name="age_declaration")
	private int ageDeclaration;
	
	@Min(value=100, message="requires 100 minimum")
	@Column(name="power_of_attorney")
	private int powerOfAttorney;
	
	@Min(value=100, message="requires 100 minimum")
	@Column(name="gift_deed")
	private int giftDeed;
	
	@Min(value=100, message="requires 100 minimum")
	@Column(name="notarize_document_per_page")
	private int notarizeDocument;
	
	public ServiceCostEntity() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getMissingItem() {
		return missingItem;
	}

	public void setMissingItem(int missingItem) {
		this.missingItem = missingItem;
	}

	public int getChangeOfName() {
		return changeOfName;
	}

	public void setChangeOfName(int changeOfName) {
		this.changeOfName = changeOfName;
	}

	public int getAgeDeclaration() {
		return ageDeclaration;
	}

	public void setAgeDeclaration(int ageDeclaration) {
		this.ageDeclaration = ageDeclaration;
	}

	public int getPowerOfAttorney() {
		return powerOfAttorney;
	}

	public void setPowerOfAttorney(int powerOfAttorney) {
		this.powerOfAttorney = powerOfAttorney;
	}

	public int getGiftDeed() {
		return giftDeed;
	}

	public void setGiftDeed(int giftDeed) {
		this.giftDeed = giftDeed;
	}

	public int getNotarizeDocument() {
		return notarizeDocument;
	}

	public void setNotarizeDocument(int notarizeDocument) {
		this.notarizeDocument = notarizeDocument;
	}

	

}
