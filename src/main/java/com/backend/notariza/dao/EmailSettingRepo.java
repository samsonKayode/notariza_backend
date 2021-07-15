package com.backend.notariza.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.notariza.entity.EmailSettingEntity;

public interface EmailSettingRepo extends JpaRepository<EmailSettingEntity, Integer> {

}
