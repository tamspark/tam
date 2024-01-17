package com.sparklab.TAM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isEnabled;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;

    private LocalDateTime forgetPasswordTokenCreationDate;
    private String forgetPasswordToken;
    private String confirmationToken;
    private LocalDateTime tokenCreationDate;
    private LocalDateTime tokenConfirmationDate;




    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;

    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


}
