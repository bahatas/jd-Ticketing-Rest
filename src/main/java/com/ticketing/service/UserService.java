package com.ticketing.service;


import com.ticketing.dto.RoleDTO;
import com.ticketing.dto.UserDTO;
import com.ticketing.entity.User;

import java.util.List;


public interface UserService {


    List<UserDTO> listAllUser();
    UserDTO findByUserName(String username);
    void save(UserDTO userDTO);
    UserDTO update(UserDTO userDTO);
    void delete(String username);

    void deleteByUserName(String username);

    List<UserDTO> listAllByRole(String role);



}
