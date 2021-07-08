package com.ticketing.service;


import com.ticketing.dto.RoleDTO;
import com.ticketing.dto.UserDTO;
import com.ticketing.entity.User;
import com.ticketing.exception.TicketingProjectException;
import org.springframework.stereotype.Service;

import java.util.List;



public interface UserService {


    List<UserDTO> listAllUser();
    UserDTO findByUserName(String username);
    UserDTO save(UserDTO userDTO) throws TicketingProjectException;
    UserDTO update(UserDTO userDTO) throws TicketingProjectException;
    void delete(String username);

    void deleteByUserName(String username);

    List<UserDTO> listAllByRole(String role);

    UserDTO confirm (User user);


}
