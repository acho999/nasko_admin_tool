package com.angel.orchestrator_service.services.impl;

import com.angel.orchestrator_service.entities.User;
import com.angel.orchestrator_service.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository repository;


    @Autowired
    public UsersDetailsServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = this.repository.findUserByUserName(username);
//            List<GrantedAuthority> authorities = user.getAuthorities()
//                .stream()
//                .map(x-> new SimpleGrantedAuthority(x.getAuthority()))
//                .collect(
//                Collectors.toList());
//            SecurityContext context = SecurityContextHolder.createEmptyContext();
//            Authentication authentication =
//                new UsernamePasswordAuthenticationToken(username, user.getEncryptedPassword(), authorities);
//            context.setAuthentication(authentication);
//            SecurityContextHolder.setContext(context);

            return user;

        } catch (UsernameNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        return null;
    }
}
