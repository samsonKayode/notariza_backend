package com.backend.notariza.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.notariza.dto.AwaitingVerificationDTO;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.UserEntity;


public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
	
	public PaymentEntity findByReference(String reference);
	
	public List<PaymentEntity> findByStatus(String status);
	
	public List<PaymentEntity> findByUserEntityOrderByDateDesc(UserEntity userEntity);
	
	public List<PaymentEntity> findByService(String service);
	
	public List<PaymentEntity> findByDateBetween(Date startDate, Date EndDate);
	
	public Boolean existsByReference(String reference);
	
	//using Example..
	@Query("SELECT p FROM PaymentEntity p WHERE (:service is null or p.service = :service) and (:reference is null"
			  + " or p.reference = :reference) and p.date between :startDate and :endDate ")
	public List<PaymentEntity> findByServiceAndReferenceAndDate(@Param("service") String service, @Param("reference") String reference, 
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	
	@Query("SELECT p FROM PaymentEntity p WHERE (:service is null or p.service = :service) and (:reference is null"
			  + " or p.reference = :reference) and p.date between :startDate and :endDate ")
	public List<AwaitingVerificationDTO> findByServiceAndReferenceAndDate1(@Param("service") String service, @Param("reference") String reference, 
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	

}
