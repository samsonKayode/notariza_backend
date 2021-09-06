package com.backend.notariza.dao;

import com.backend.notariza.dto.ServiceNumbersDTO;
import com.backend.notariza.entity.PaymentEntity;
import com.backend.notariza.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceNumbersRepo extends JpaRepository<PaymentEntity, Integer> {

    @Query("SELECT p.service, COUNT(*) as no_of_service from PaymentEntity as p WHERE p.userEntity = :userEntity GROUP BY p.service ORDER BY p.service ASC")
    public List<ServiceNumbersDTO> groupBy(@Param("userEntity") UserEntity userEntity);
}
