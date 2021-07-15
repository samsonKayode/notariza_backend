package com.backend.notariza.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.PaymentRepository;
import com.backend.notariza.dao.UserDao;
import com.backend.notariza.dto.DashBoardListDTO;
import com.backend.notariza.entity.PaymentEntity;

@Service
public class DashboardService {

	@Autowired
	PaymentRepository paymentRepo;

	@Autowired
	CurrentUser currentUser;

	@Autowired
	UserDao userDao;

	Logger log = LoggerFactory.getLogger(this.getClass());

	Date date;

	String service;

	String reference;

	int customerID;

	int sn = 0;

	// get dashboard service list for current user..

	public List<DashBoardListDTO> getListForCurrentUser() {

		DashBoardListDTO dashBoardList;
		
		sn=0;

		ArrayList<DashBoardListDTO> arrayList = new ArrayList<DashBoardListDTO>();

		List<PaymentEntity> list1 = paymentRepo.findByUserEntityOrderByDateDesc(currentUser.getUserEntity());

		for (int i = 0; i < list1.size(); i++) {

			sn++;

			PaymentEntity paymentEntity = list1.get(i);

			date = paymentEntity.getDate();
			service = paymentEntity.getService();
			reference = paymentEntity.getReference();
			customerID = currentUser.getUserId();

			dashBoardList = new DashBoardListDTO();

			dashBoardList.setCustomerID(customerID);
			dashBoardList.setDate(date);
			dashBoardList.setReference(reference);
			dashBoardList.setService(service);
			dashBoardList.setId(sn);

			arrayList.add(dashBoardList);
		}

		return arrayList;

	}

	// dashboardList for any user.. admin module..

	public List<DashBoardListDTO> getListForAnyUser(int userId) {

		sn = 0;

		DashBoardListDTO dashBoardList;

		ArrayList<DashBoardListDTO> arrayList = new ArrayList<DashBoardListDTO>();

		List<PaymentEntity> list1 = paymentRepo.findByUserEntityOrderByDateDesc(userDao.findById(userId));

		for (int i = 0; i < list1.size(); i++) {

			sn++;

			PaymentEntity paymentEntity = list1.get(i);

			date = paymentEntity.getDate();
			service = paymentEntity.getService();
			reference = paymentEntity.getReference();
			customerID = currentUser.getUserId();

			dashBoardList = new DashBoardListDTO();

			dashBoardList.setCustomerID(customerID);
			dashBoardList.setDate(date);
			dashBoardList.setReference(reference);
			dashBoardList.setService(service);
			dashBoardList.setId(sn);

			arrayList.add(dashBoardList);
		}

		return arrayList;

	}

}
