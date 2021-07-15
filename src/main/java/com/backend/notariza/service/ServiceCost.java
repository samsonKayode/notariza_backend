package com.backend.notariza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.ServiceCostRepo;
import com.backend.notariza.dao.UserDao;
import com.backend.notariza.entity.ServiceCostEntity;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.pojos.ActionResult;

@Service
public class ServiceCost {

	@Autowired
	ServiceCostRepo serviceRepo;

	@Autowired
	UserDao userDao;

	@Autowired
	CurrentUser currentUser;

	@Value("${userType}")
	String userType;

	// save service Cost..

	public ServiceCostEntity saveServiceCost(ServiceCostEntity serviceCostEntity) {

		return serviceRepo.save(serviceCostEntity);
	}

	// delete service cost type..

	public ActionResult deleteService(int id) {

		ActionResult ar = null;

		String str = "";

		ServiceCostEntity serviceCostEntity = serviceRepo.getById(id);

		String userType = serviceCostEntity.getUserType();

		// find users with service cost type..

		List<UserEntity> theList = userDao.findByUserType(userType);

		if (theList.size() > 0) {
			str = "Unable to delete service cost, users are currently attached to it";
			ar = new ActionResult(1, str, System.currentTimeMillis());
		} else {
			serviceRepo.delete(serviceCostEntity);
			str = "item deleted";
			ar = new ActionResult(0, str, System.currentTimeMillis());
		}

		return ar;
	}

	// list all service costs available..

	public List<ServiceCostEntity> listAll() {

		return serviceRepo.findAll();
	}

}
