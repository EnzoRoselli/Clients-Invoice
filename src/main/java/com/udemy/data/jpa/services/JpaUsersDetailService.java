package com.udemy.data.jpa.services;

import com.udemy.data.jpa.dao.IUserDao;
import com.udemy.data.jpa.models.entities.Role;
import com.udemy.data.jpa.models.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jpaUserDetailsService")
public class JpaUsersDetailService implements UserDetailsService {
    @Autowired
    private IUserDao userDao;

    private Logger logger = LoggerFactory.getLogger(JpaUsersDetailService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        if (user == null){
            logger.error("Error login: no existe el usuario " + username);
            throw new UsernameNotFoundException("Username " + username + " no existe en el sistema");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role: user.getRoles()) {
            logger.info("Role: ".concat(role.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        if (authorities.isEmpty()){
            logger.error("Error login: usuario " + username + " no tiene roles asignados");
            throw new UsernameNotFoundException("Username " + username + " no tiene roles asignados");
        }

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), user.isEnable(), true, true, true, authorities);
    }
}
