package com.ticketing.implementation;

import com.ticketing.dto.UserDTO;
import com.ticketing.entity.User;

import com.ticketing.mapper.UserMapper;
import com.ticketing.repository.UserRepository;
import com.ticketing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl  implements UserService {


    UserRepository userRepository;


    UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> listAllUser() {
        List<User> list = userRepository.findAll(Sort.by("firstName"));

        //convert entity to DTO
        return list.stream().map(each->{return userMapper.convertToDto(each); }).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {

        User user=userRepository.findByUserName(username);
        //converto to DTO
        return userMapper.convertToDto(user);
    }



    @Override
    public void save(UserDTO userDTO) {


        //dto to entity
       User user= userMapper.convertToEntity(userDTO);

        //save with implemented method via Jpa
        userRepository.save(user);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {

        //Find current user
        User user = userRepository.findByUserName(userDTO.getUserName());
        //map update user dto to entity object
        User convertedUser=userMapper.convertToEntity(userDTO);
        //set id to the converted object
        convertedUser.setId(user.getId());

        //savd updated user
        userRepository.save(convertedUser);


        return findByUserName(userDTO.getUserName());
    }

    @Override
    public void delete(String username) {

    }

    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);

        return users.stream().map(each->{return userMapper.convertToDto(each);}).collect(Collectors.toList());
    }
}
