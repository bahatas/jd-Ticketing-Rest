package com.ticketing.implementation;

import com.ticketing.dto.UserDTO;
import com.ticketing.entity.User;
import com.ticketing.service.SecurityService;
import com.ticketing.service.UserService;
import com.ticketing.utils.MapperUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {
    private UserService userService;
    private MapperUtil mapperUtil;

    public SecurityServiceImpl(UserService userService, MapperUtil mapperUtil) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public User loadUser(String username) {

        UserDTO user = userService.findByUserName(username);

        return mapperUtil.convert(user,new User()) ;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserDTO user = userService.findByUserName(s);

        if (user == null) {
            throw new UsernameNotFoundException("This user not found ");
        }

        return new org.springframework.security.core.userdetails.User(user.getId().toString(), user.getPassWord(),listAuthorities(user) );

    }

    private Collection<? extends GrantedAuthority> listAuthorities(UserDTO user) {
        List<GrantedAuthority> authorityList = new ArrayList<>();

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getDescription());
        authorityList.add(authority);

        return authorityList;
    }


}
