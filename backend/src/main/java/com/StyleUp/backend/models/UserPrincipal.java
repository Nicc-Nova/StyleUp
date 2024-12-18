package com.StyleUp.backend.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getFname() {
        return user.getFname();
    }

    public String getLname() {
        return user.getLname();
    }

    public Long getId() {
        return user.getId();
    }

    public List<Room> getRooms() {
        return user.getRooms();
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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + this.getId() +
                ", fname='" + this.getFname() + '\'' +
                ", lname='" + this.getLname() + '\'' +
                ", email='" + this.getUsername() + '\'' +
                ", password='" + this.getPassword() + '\'' +
                //", rooms=" + this.getRooms().toString() +
                '}';
    }
}
