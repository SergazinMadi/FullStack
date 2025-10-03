package org.example.fullstack.service;

import org.example.fullstack.db.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    User getByUsername(String username);
    UserDetailsService userDetailsService();
}
