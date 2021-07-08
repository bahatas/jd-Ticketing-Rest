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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller", description = "User API")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<ResponseWrapper> readAll() {
        List<UserDTO> result = userService.listAllUser();

        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all  users", result));
    }


}
