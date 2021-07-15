package com.backend.notariza.rest;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.notariza.dao.UserDao;
import com.backend.notariza.entity.UserEntity;
import com.backend.notariza.jwt.JwtRequest;
import com.backend.notariza.jwt.JwtTokenUtil;
import com.backend.notariza.pojos.ActionResult;
import com.backend.notariza.pojos.LoginResponse;
import com.backend.notariza.service.CurrentUser;
import com.backend.notariza.service.JwtUserDetailsService;


@RestController
@CrossOrigin
public class UserRest {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	CurrentUser currentUser;

	@Autowired
	UserDao userDao;
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/v1/user/authenticate", method = RequestMethod.POST)
	public LoginResponse createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest)
			throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		System.out.println("Token Expiration===>>" + jwtTokenUtil.getExpirationDateFromToken(token));

		UserEntity userEntity = userDetailsService.getOneByUsername(userDetails.getUsername());

		LoginResponse loginResponse = new LoginResponse();

		loginResponse.setId(userEntity.getId());
		loginResponse.setSex(userEntity.getSex());
		loginResponse.setAddress(userEntity.getAddress());
		loginResponse.setDob(userEntity.getDob());
		loginResponse.setFirstname(userEntity.getFirstname());
		loginResponse.setLastname(userEntity.getLastname());
		loginResponse.setOthernames(userEntity.getOthernames());
		loginResponse.setUsername(userEntity.getUsername());
		loginResponse.setPassword(userEntity.getPassword());
		loginResponse.setToken(token);

		loginResponse.setRoles(userEntity.getRoles());

		return loginResponse;
	}

	@RequestMapping(value = "/v1/user/register", method = RequestMethod.POST)
	public UserEntity saveUser(@Valid @RequestBody UserEntity user) throws MethodArgumentNotValidException, Exception {

		return userDetailsService.save(user);

	}
	
	// verify user account..

	@GetMapping("/v1/user/verify")
	public ActionResult verifyAccount(@RequestParam(value = "code") final String verificationCode) throws Exception {

		ActionResult actionResult = null;

		log.info("Verifying account for "+verificationCode);
		userDetailsService.verifyAccount(verificationCode);
		actionResult = new ActionResult(0, "Account  verified", System.currentTimeMillis());
		log.info("Account verified "+verificationCode);
		
		return actionResult;

	}

	// update user..
	@PutMapping("/v1/service/user/update")
	public UserEntity updateUser(@RequestParam(value = "userID") final int userID,
			@Valid @RequestBody UserEntity user) {

		return userDetailsService.update(user, userID);

	}
	

	
	
	@DeleteMapping("/v1/admin/user/delete/{id}")
	public ActionResult deleteUser(@PathVariable int id) {

		String message = "";

		int result = userDetailsService.deleteUser(id);

		if (result > 0) {
			message = "error deleting user";
		}

		return new ActionResult(result, message, System.currentTimeMillis());

	}

	// get user by username..

	@GetMapping("/v1/admin/user/find/email")
	public UserEntity getByEmail(@RequestParam(value = "email") final String email) {

		return userDetailsService.getOneByUsername(email);
	}

	@GetMapping("/v1/admin/user/find/id")
	public UserEntity getById(@PathVariable int id) {

		return userDetailsService.getOneById(id);
	}

	// get All Users...

	@GetMapping("/v1/admin/user/find")
	public List<UserEntity> getAll() {

		return userDetailsService.getAllUsers();
	}

	// get user by id..

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
