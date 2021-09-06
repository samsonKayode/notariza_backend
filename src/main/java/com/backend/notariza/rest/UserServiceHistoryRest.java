package com.backend.notariza.rest;

import java.util.List;

import com.backend.notariza.dto.ServiceNumbersDTO;
import com.backend.notariza.service.DashboardNumbersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.dto.DashBoardListDTO;
import com.backend.notariza.service.DashboardService;

@RestController
@CrossOrigin
@RequestMapping("/v1/service/logs")
public class UserServiceHistoryRest {

	@Autowired
	DashboardService dashboardService;

	@Autowired
	DashboardNumbersService dashboardNumbersService;

	@GetMapping("/list")
	public List<DashBoardListDTO> getUserHistory() {

		return dashboardService.getListForCurrentUser();
	}

	@GetMapping("/data")
	public List<ServiceNumbersDTO> getData(){
		return dashboardNumbersService.getData();
	}

	@GetMapping("/user")
	public List<DashBoardListDTO> getUserHistory(@RequestParam(value = "id") int id) {

		return dashboardService.getListForAnyUser(id);
	}

}
