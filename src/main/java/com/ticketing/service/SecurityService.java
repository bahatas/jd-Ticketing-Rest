package com.ticketing.service;

import com.ticketing.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService extends UserDetailsService {

    User loadUser(String username);
}
