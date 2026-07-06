package org.example.backend.user.service;

import org.example.backend.user.dto.LoginRequest;
import org.example.backend.user.dto.RegisterRequest;
import org.example.backend.user.dto.UserResponse;
import org.example.backend.user.entity.User;

import java.util.List;

public interface IUserService {
    public List<User> findAll();
    public User findById(long id);
    public void save(User user);
    public void deleteById(long id);
    
    UserResponse register(RegisterRequest request);
    UserResponse login(LoginRequest request);
}
