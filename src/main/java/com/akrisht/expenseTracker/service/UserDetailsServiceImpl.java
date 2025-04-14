package com.akrisht.expenseTracker.service;

import com.akrisht.expenseTracker.entity.User;
import com.akrisht.expenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isPresent()) {
            User u = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(u.getEmail())
                    .password(u.getPassword())
                    .roles("USER") // Or fetch roles from the user entity if available
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }
    }
}
