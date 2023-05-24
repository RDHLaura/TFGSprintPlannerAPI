package com.tfg.sprintplannerapi.bo;

import com.tfg.sprintplannerapi.dao.UserRepository;
import com.tfg.sprintplannerapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceBO implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmailIgnoreCase(username).orElse(null);

        if (user != null) {
            org.springframework.security.core.userdetails.User.UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);

            //TODO establecer clase roles
            userBuilder.password(user.getPassword()).roles("USER");
            return userBuilder.build();
        } else {
            throw new UsernameNotFoundException(username);
        }

    }



}
