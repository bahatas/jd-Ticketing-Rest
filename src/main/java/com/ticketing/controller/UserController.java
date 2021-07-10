package com.ticketing.controller;

import com.ticketing.annotation.DefaultExceptionMessage;
import com.ticketing.dto.MailDTO;
import com.ticketing.dto.UserDTO;
import com.ticketing.entity.ConfirmationToken;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.entity.User;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.service.ConfirmationTokenService;
import com.ticketing.service.RoleService;
import com.ticketing.service.UserService;
import com.ticketing.utils.JWTUtil;
import com.ticketing.utils.MapperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller", description = "User API")
public class UserController {

    @Value("${app.local-url}")
    private String BASE_URL;


    private final UserService userService;
    private final MapperUtil mapperUtil;
    private final ConfirmationTokenService confirmationTokenService;

    public UserController(UserService userService, MapperUtil mapperUtil, ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
        this.confirmationTokenService = confirmationTokenService;
    }

    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @PostMapping("/create-user")
    @Operation(summary = "create new account")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> doRegister(@RequestBody UserDTO userDTO) throws TicketingProjectException {

        UserDTO createdUser = userService.save(userDTO);
       // sendEmail(createEmail(createdUser));


        return ResponseEntity.ok(new ResponseWrapper("User has been created", createdUser));


    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Read All Users")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> readAll() {
        List<UserDTO> result = userService.listAllUser();

        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all  users", result));
    }


    @GetMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Read by Username")
    //Only admin should see other profiles or current user can see his/her profile
    public ResponseEntity<ResponseWrapper> readByUsername(@PathVariable String username){
        UserDTO userDTO = userService.findByUserName(username);

        return ResponseEntity.ok(new ResponseWrapper("Succesfully retrieved user",userDTO));
    }

    @PutMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Update User")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user) throws TicketingProjectException {
        UserDTO updatedUser = userService.update(user);

        return ResponseEntity.ok(new ResponseWrapper("Succesfully updated",updatedUser));
    }


    @DeleteMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Delete User")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username){



        userService.deleteByUserName(username);

        return ResponseEntity.ok(new ResponseWrapper("Succesfully deleted"));
    }


    @GetMapping("/role")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Read by role")
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> readByRole(@RequestParam String role){

        List<UserDTO> userlist = userService.listAllByRole(role);


        return ResponseEntity.ok(new ResponseWrapper("Succesfully read users by role",userlist));
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
