package com.paydaybank.dashboard.config.security.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.paydaybank.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<com.paydaybank.dashboard.model.User> user = userRepository.findByEmail(email);
        if( !user.isPresent()  ) {
            throw new UsernameNotFoundException("Email: " + email + " not found");
        }

        List<GrantedAuthority> grantedAuthorities = user.get().getRoles().stream()
                                                    .map(a -> new SimpleGrantedAuthority(a.getRole().toString()))
                                                    .collect(Collectors.toList());

        return new User(user.get().getId().toString(), user.get().getPassword(), grantedAuthorities);
    }
}