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
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "change_of_name")
public class ChangeOfNameEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="date")
	private Date date;
	
	@NotBlank(message = "surname is mandatory")
	@Size(min=2)
	@Column(name="surname")
	private String surname;
	
	@NotBlank(message = "firstname is mandatory")
	@Size(min=2)
	@Column(name="firstname")
	private String firstname;
	
	@NotBlank(message = "address is mandatory")
	@Size(min=5)
	@Column(name="address")
	private String address;
	
	@PastOrPresent(message="you are not from the future")
	@Column(name="dob")
	private Date dob;
	
	@NotBlank(message = "sex is mandatory")
	@Size(min=4)
	@Column(name="sex")
	private String sex;
	
	@NotBlank(message = "former name is mandatory")
	@Size(min=5)
	@Column(name="former_name")
	private String former_name;
	
	@Column(name="status")
	private String status;
	
	@Column(name="reference")
	private String reference;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable=false)
	private UserEntity userEntity;

	
	public ChangeOfNameEntity() {
		
	}


	public ChangeOfNameEntity(String surname, String firstname, String address, Date dob, String sex,
			String former_name, Date date) {
		super();
		this.surname = surname;
		this.firstname = firstname;
		this.address = address;
		this.dob = dob;
		this.sex = sex;
		this.former_name = former_name;
		this.date = date;
	}




	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
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


	public Date getDob() {
		return dob;
	}


	public void setDob(Date dob) {
		this.dob = dob;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getFormer_name() {
		return former_name;
	}


	public void setFormer_name(String former_name) {
		this.former_name = former_name;
	}


	public UserEntity getUserEntity() {
		return userEntity;
	}


	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
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


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
