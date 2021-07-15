package com.backend.notariza.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Email(message = "Email should be valid")
	@Column(name = "username", unique = true)
	private String username;

	@NotBlank(message = "password is mandatory")
	@Size(min = 5)
	@Column(name = "password")
	private String password;

	@NotNull
	@NotBlank(message = "lastname is mandatory")
	@Size(min = 2)
	@Column(name = "lastname")
	private String lastname;

	@NotNull
	@NotBlank(message = "firstname is mandatory")
	@Size(min = 2)
	@Column(name = "firstname")
	private String firstname;

	@Column(name = "othernames")
	private String othernames;

	@NotNull
	@NotBlank(message = "sex is mandatory")
	@Column(name = "sex")
	@Size(min = 4)
	private String sex;

	@Column(name = "dob")
	private Date dob;

	@NotBlank(message = "address is mandatory")
	@Size(min = 5)
	@Column(name = "address")
	private String address;

	@Column(name = "user_type", columnDefinition = "varchar(255) default 'GENERAL USER'")
	private String userType;
	
	private boolean enabled;
	
	private String verificationCode;

	@Transient
	private String userRoleName;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userEntity")
	private Set<ChangeOfNameEntity> changeOfNameEntity;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userEntity")
	private Set<AgeDeclarationEntity> ageDeclarationEntity;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userEntity")
	private Set<GiftDeedEntity> giftDeedEntity;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userEntity")
	private Set<NotarizeDocumentEntity> notarizeDocumentEntity;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userEntity")
	private Set<PaymentEntity> paymentEntity;

	public UserEntity() {

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getOthernames() {
		return othernames;
	}

	public void setOthernames(String othernames) {
		this.othernames = othernames;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<ChangeOfNameEntity> getChangeOfNameEntity() {
		return changeOfNameEntity;
	}

	public void setChangeOfNameEntity(Set<ChangeOfNameEntity> changeOfNameEntity) {
		this.changeOfNameEntity = changeOfNameEntity;
	}

	public Set<PaymentEntity> getPaymentEntity() {
		return paymentEntity;
	}

	public void setPaymentEntity(Set<PaymentEntity> paymentEntity) {
		this.paymentEntity = paymentEntity;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<AgeDeclarationEntity> getAgeDeclarationEntity() {
		return ageDeclarationEntity;
	}

	public void setAgeDeclarationEntity(Set<AgeDeclarationEntity> ageDeclarationEntity) {
		this.ageDeclarationEntity = ageDeclarationEntity;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	public Set<GiftDeedEntity> getGiftDeedEntity() {
		return giftDeedEntity;
	}

	public void setGiftDeedEntity(Set<GiftDeedEntity> giftDeedEntity) {
		this.giftDeedEntity = giftDeedEntity;
	}

	public Set<NotarizeDocumentEntity> getNotarizeDocumentEntity() {
		return notarizeDocumentEntity;
	}

	public void setNotarizeDocumentEntity(Set<NotarizeDocumentEntity> notarizeDocumentEntity) {
		this.notarizeDocumentEntity = notarizeDocumentEntity;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	

}