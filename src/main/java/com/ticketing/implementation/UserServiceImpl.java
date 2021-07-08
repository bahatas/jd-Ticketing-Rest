package com.ticketing.implementation;

import com.ticketing.dto.UserDTO;
import com.ticketing.entity.User;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.repository.UserRepository;
import com.ticketing.service.UserService;
import com.ticketing.utils.MapperUtil;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
//    private ProjectService projectService;
//    private TaskService taskService;
    private MapperUtil mapperUtil;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> listAllUser() {
        List<User> list = userRepository.findAll(Sort.by("firstName"));

        //convert entity to DTO
    return list.stream().map(each->mapperUtil.convert(each, new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {

        User user=userRepository.findByUserName(username);
        //converto to DTO

        return mapperUtil.convert(user,new UserDTO());
    }


    @Override
    public UserDTO save(UserDTO userDTO) throws TicketingProjectException {

        User foundUser = userRepository.findByUserName(userDTO.getUserName());

        if (foundUser != null) {
            throw new TicketingProjectException("User Already exist");
        }



        User user = mapperUtil.convert(userDTO,new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User save = userRepository.save(user);


        return mapperUtil.convert(save,new UserDTO());
    }

    @Override
    public UserDTO update(UserDTO dto) throws TicketingProjectException, AccessDeniedException {

        //Find current user
        User user = userRepository.findByUserName(dto.getUserName());

        if(user == null){
            throw new TicketingProjectException("User Does Not Exists");
        }
        //Map update user dto to entity object
        User convertedUser = mapperUtil.convert(dto,new User());
        convertedUser.setPassword(passwordEncoder.encode(convertedUser.getPassword()));
        if(!user.getEnabled()){
            throw new TicketingProjectException("User is not confirmed");
        }

        checkForAuthorities(user);

        convertedUser.setEnabled(true);

        //set id to the converted object
        convertedUser.setId(user.getId());
        //save updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
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

        return users.stream().map(each->{return mapperUtil.convert(each,new UserDTO());}).collect(Collectors.toList());
    }

    @Override
    public UserDTO confirm(User user) {
        user.setEnabled(true);
        User confirmedUser = userRepository.save(user);

        return mapperUtil.convert(confirmedUser,new UserDTO());
    }

    public void checkForAuthorities(User user){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    }
}
