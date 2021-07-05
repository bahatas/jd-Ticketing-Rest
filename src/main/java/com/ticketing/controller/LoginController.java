package com.ticketing.controller;

import com.ticketing.annotation.DefaultExceptionMessage;
import com.ticketing.dto.UserDTO;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.entity.User;
import com.ticketing.entity.common.AuthenticationRequest;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.service.UserService;
import com.ticketing.utils.JWTUtil;
import com.ticketing.utils.MapperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
//public class LoginController {
//
//	@RequestMapping("/")
//	public String login(){
//		return "login";
//	}
//
//	@RequestMapping("/welcome")
//	public String welcome(){
//		return "welcome";
//	}
//
//}

@RestController
@Tag(name="Authentication Controller", description = "Authenticate API")
public class LoginController {

	private JWTUtil jwtUtil;
	private UserService userService;
	private MapperUtil mapperUtil;
	private AuthenticationManager  authenticationManager;

	public LoginController(JWTUtil jwtUtil, UserService userService, MapperUtil mapperUtil, AuthenticationManager authenticationManager) {
		this.jwtUtil = jwtUtil;
		this.userService = userService;
		this.mapperUtil = mapperUtil;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/authenticate")
	@DefaultExceptionMessage(defaultMessage = "Bad credentials")
	@Operation(summary = "Login to application")
	public ResponseEntity<ResponseWrapper>  doLogin(@RequestBody AuthenticationRequest authenticationRequest) throws TicketingProjectException {

		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();

//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
//		authenticationManager.authenticate(authenticationToken); // debug

		UserDTO foundUser= userService.findByUserName(username);
		User convertedUser=mapperUtil.convert(foundUser, new User());

		if(!foundUser.isEnabled()){
			throw new TicketingProjectException("Please verify you user");
		}
		String jwtToken = jwtUtil.generateToken(convertedUser);
		return ResponseEntity.ok(new ResponseWrapper("Login successfull",jwtToken));
	}
}
