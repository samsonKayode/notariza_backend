package com.backend.notariza.service;

import com.backend.notariza.dao.ServiceNumbersRepo;
import com.backend.notariza.dto.ServiceNumbersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardNumbersService {

    @Autowired
    ServiceNumbersRepo serviceNumbersRepo;

    @Autowired
    CurrentUser currentUser;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //retrieve data..
    public List<ServiceNumbersDTO> getData(){

        return serviceNumbersRepo.groupBy(currentUser.getUserEntity());
    }
}
