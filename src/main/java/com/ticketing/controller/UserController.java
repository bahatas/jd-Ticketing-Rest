package com.ticketing.controller;

import com.ticketing.dto.UserDTO;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.service.ConfirmationTokenService;
import com.ticketing.service.RoleService;
import com.ticketing.service.UserService;
import com.ticketing.utils.JWTUtil;
import com.ticketing.utils.MapperUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller", description = "User API")
public class UserController {

    @Value("${app.local-url}")
    private String BASE_URL;

    private JWTUtil jwtUtil;
    private UserService userService;
    private MapperUtil mapperUtil;
    private RoleService roleService;
    private ConfirmationTokenService confirmationTokenService;

    public UserController(JWTUtil jwtUtil, UserService userService, MapperUtil mapperUtil,
                          RoleService roleService, ConfirmationTokenService confirmationTokenService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.mapperUtil = mapperUtil;
        this.roleService = roleService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> readAll() {
        List<UserDTO> result = userService.listAllUser();

        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all  users", result));
    }


    @GetMapping("/username")
    //Only admin should see other profiles or current user can see his/her profile
    public ResponseEntity<ResponseWrapper> readByUsername(@PathVariable String username){
        UserDTO userDTO = userService.findByUserName(username);

        return ResponseEntity.ok(new ResponseWrapper("Succesfully retrieved user",userDTO));
    }

}
