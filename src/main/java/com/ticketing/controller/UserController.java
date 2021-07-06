package com.ticketing.controller;

import com.ticketing.dto.MailDTO;
import com.ticketing.dto.UserDTO;
import com.ticketing.entity.ConfirmationToken;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.entity.User;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.service.ConfirmationTokenService;
import com.ticketing.service.UserService;
import com.ticketing.utils.MapperUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller", description = "User API")
public class UserController {

    @Value("${app.local-url}")
    private String BASE_URL;

    private UserService userService;
    private ConfirmationTokenService confirmationTokenService;
    private MapperUtil mapperUtil;


    public UserController(UserService userService, ConfirmationTokenService confirmationTokenService, MapperUtil mapperUtil) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.mapperUtil = mapperUtil;
    }


    @PostMapping("/create-user")
    public ResponseEntity<ResponseWrapper> doRegister(@RequestBody UserDTO userDTO) throws TicketingProjectException {

        UserDTO createdUser = userService.save(userDTO);
        sendEmail(createEmail(createdUser));



        return ResponseEntity.ok(new ResponseWrapper("User has been created",createdUser));


    }


    private void sendEmail(MailDTO mailDTO) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mailDTO.getEmailTo());
        simpleMailMessage.setSubject(mailDTO.getSubject());
        simpleMailMessage.setText(mailDTO.getMessage()+mailDTO.getUrl()+mailDTO.getToken());
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
