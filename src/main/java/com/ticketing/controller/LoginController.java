package com.ticketing.controller;

import com.ticketing.annotation.DefaultExceptionMessage;
import com.ticketing.dto.MailDTO;
import com.ticketing.dto.UserDTO;
import com.ticketing.entity.ConfirmationToken;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.entity.User;
import com.ticketing.entity.common.AuthenticationRequest;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.service.ConfirmationTokenService;
import com.ticketing.service.UserService;
import com.ticketing.utils.JWTUtil;
import com.ticketing.utils.MapperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Authentication Controller", description = "Authenticate API")
public class LoginController {
	@Value("${app.local-url}")
	private String BASE_URL;

	private JWTUtil jwtUtil;
	private UserService userService;
	private MapperUtil mapperUtil;
	private final AuthenticationManager authenticationManager;
	@Autowired
	private ConfirmationTokenService confirmationTokenService;

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

		UserDTO foundUser = userService.findByUserName(username);
		User convertedUser = mapperUtil.convert(foundUser, new User());

		if (!foundUser.isEnabled()) {
			throw new TicketingProjectException("Please verify you user");
		}
		String jwtToken = jwtUtil.generateToken(convertedUser);
		return ResponseEntity.ok(new ResponseWrapper("Login successfull", jwtToken));
	}

	@PostMapping("/create-user")
	public ResponseEntity<ResponseWrapper> doRegister(@RequestBody UserDTO userDTO) throws TicketingProjectException {

		UserDTO createdUser = userService.save(userDTO);
		sendEmail(createEmail(createdUser));


		return ResponseEntity.ok(new ResponseWrapper("User has been created", createdUser));


	}

	@GetMapping("/confirmation")
	public ResponseEntity<ResponseWrapper> confirmEmail(@RequestParam("token") String token) throws TicketingProjectException {

		ConfirmationToken confirmationToken = confirmationTokenService.readByToken(token);
		UserDTO confirmed = userService.confirm(confirmationToken.getUser());
		confirmationTokenService.delete(confirmationToken);

		return ResponseEntity.ok(new ResponseWrapper("User has been confirmed", confirmed));


	}

	private void sendEmail(MailDTO mailDTO) {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(mailDTO.getEmailTo());
		simpleMailMessage.setSubject(mailDTO.getSubject());
		simpleMailMessage.setText(mailDTO.getMessage() + mailDTO.getUrl() + mailDTO.getToken());
		confirmationTokenService.sendEmail(simpleMailMessage);
	}

	private MailDTO createEmail(UserDTO userDTO) {
		User user = mapperUtil.convert(userDTO, new User());
		ConfirmationToken confirmationToken = new ConfirmationToken(user);
		confirmationToken.setIsDeleted(false);

		ConfirmationToken createdConfirmationToken = confirmationTokenService.save(confirmationToken);

		return MailDTO.builder()
				.emailTo(user.getUserName())
				.token(createdConfirmationToken.getToken())
				.subject("Confirm Registration")
				.message("To Confirm your account, please click here")
				.url(BASE_URL + "/confirmation?token=")
				.build();


	}
}
