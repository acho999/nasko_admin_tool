package com.angel.orchestrator_service.entities;

import com.angel.orchestrator_service.pojo.Token;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;


@Builder
public class UserPrincipal implements UserDetails {

    private UUID id;
    private String userName;
    private String email;
    private String encryptedPassword;
    private List<GrantedAuthority> authorities;
    private User user;
    private Date date_created;
    private List<Token> tokens;

    @PostConstruct
    public void init(User userEntity) {
        this.authorities = userEntity
            .getAuthorities()
            .stream()
            .map(x -> new SimpleGrantedAuthority(x.getAuthority()))
            .collect(Collectors.toList());
    }

    public String getEmail() {
        return this.email;
    }

    public String getId() {
        return this.id.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream()
            .map(x->new SimpleGrantedAuthority(x.getAuthority()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.encryptedPassword;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
