package com.backend.notariza.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.entity.ServiceCostEntity;
import com.backend.notariza.pojos.ActionResult;
import com.backend.notariza.service.ServiceCost;

@RestController
@CrossOrigin
@RequestMapping("/v1/admin/cost")
public class ServiceCostRest {
	
	@Autowired
	ServiceCost serviceCost;
	
	
	//save service Cost..
	@PostMapping("/save")
	public ServiceCostEntity save(@Valid @RequestBody ServiceCostEntity serviceCostEntity) throws MethodArgumentNotValidException, Exception {
		
		return serviceCost.saveServiceCost(serviceCostEntity);
	}
	
	//delete service cost..
	@DeleteMapping("/delete/{id}")
	public ActionResult delete(@PathVariable int id) {
		
		return serviceCost.deleteService(id);
	}
	
	//List all existing service cost..
	@GetMapping("/list")
	public List<ServiceCostEntity> listAll(){
		
		return serviceCost.listAll();
	}

}
