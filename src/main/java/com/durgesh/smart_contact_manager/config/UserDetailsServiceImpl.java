package com.durgesh.smart_contact_manager.config;

import com.durgesh.smart_contact_manager.dao.UserRepository;
import com.durgesh.smart_contact_manager.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository ur;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.ur.getUserByUserName(username);

        if (user == null)
            throw new UsernameNotFoundException("Could not found user");

        UserDetails ud = new UserDetailsImpl(user);
        return ud;
    }


    public UserRepository getUr() {
        return this.ur;
    }

    public void setUr(UserRepository ur) {
        this.ur = ur;
    }


}
