package com.jing.cloud.service.bean;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {
    
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    @Valid
    @Size(min=2)
    private List<Position> ps;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    
    public List<Position> getPs() {
        return ps;
    }

    
    public void setPs(List<Position> ps) {
        this.ps = ps;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", ps=" + ps + "]";
    }
}
