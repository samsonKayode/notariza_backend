package com.backend.notariza.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="age_declaration")
public class AgeDeclarationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="date")
	private Date date;
	
	@NotBlank(message = "lastname is mandatory")
	@Size(min=2)
	@Column(name="lastname")
	private String lastname;
	
	@NotBlank(message = "firstname is mandatory")
	@Size(min=2)
	@Column(name="firstname")
	private String firstname;
	
	@NotBlank(message = "address is mandatory")
	@Size(min=5)
	@Column(name="address")
	private String address;
	
	@NotBlank(message = "sex is mandatory")
	@Size(min=4)
	@Column(name="sex")
	private String  sex;
	
	@NotNull(message = "owner status is mandatory")
	@Column(name="owner")
	private boolean owner;
	
	@Column(name="o_name")
	private String ownerName;
	
	@Column(name="o_sex")
	private String ownerSex;
	
	@Column(name="relationship")
	private String relationshipToOwner;
	
	@Past(message="date of birth is invalid")
	@Column(name="dob")
	private Date dob;
	
	@Column(name="place_of_birth")
	@NotBlank(message = "place of birth is mandatory")
	@Size(min=5)
	private String placeOfBirth;
	
	@Column(name="reason")
	@NotBlank(message = "reason for affidavit is mandatory")
	@Size(min=3)
	private String reason;
	
	
	@Column(name="status")
	private String status;
	
	@Column(name="reference")
	private String reference;

	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable=false)
	private UserEntity userEntity;
	
	public AgeDeclarationEntity() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getRelationshipToOwner() {
		return relationshipToOwner;
	}

	public void setRelationshipToOwner(String relationshipToOwner) {
		this.relationshipToOwner = relationshipToOwner;
	}

	public String getOwnerSex() {
		return ownerSex;
	}

	public void setOwnerSex(String ownerSex) {
		this.ownerSex = ownerSex;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
}
