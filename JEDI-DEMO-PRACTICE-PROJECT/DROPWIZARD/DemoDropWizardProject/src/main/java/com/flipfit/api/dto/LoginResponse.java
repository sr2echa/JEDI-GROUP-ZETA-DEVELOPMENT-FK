package com.flipfit.api.dto;

import com.flipfit.bean.Role;

public class LoginResponse {
    private int userId;
    private String username;
    private Role role;
    private String message;
    
    public LoginResponse() {}
    
    public LoginResponse(int userId, String username, Role role, String message) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.message = message;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
