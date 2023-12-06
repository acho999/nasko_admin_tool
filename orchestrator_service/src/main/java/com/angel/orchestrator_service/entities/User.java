package com.angel.orchestrator_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @Column(nullable = false)
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column()
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    @Column(nullable = false)
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,cascade = {
        CascadeType.ALL
    })
    @JoinTable(name = "roles_users",
        joinColumns = { @JoinColumn(name = "id", referencedColumnName="id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") })
    private List<Role> authorities;
    @Column(nullable = false)
    private Date date_created;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream()
            .map(x->new SimpleGrantedAuthority(x.getName()))
            .collect(Collectors.toList());
    }

    public List<Role> getRoles() {
        return this.authorities;
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
