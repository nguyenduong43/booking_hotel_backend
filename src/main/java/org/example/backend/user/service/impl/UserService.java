package org.example.backend.user.service.impl;

import org.example.backend.role.Role;
import org.example.backend.role.repository.IRoleRepository;
import org.example.backend.user.dto.LoginRequest;
import org.example.backend.user.dto.RegisterRequest;
import org.example.backend.user.dto.UserResponse;
import org.example.backend.user.entity.User;
import org.example.backend.user.repository.IUserRepository;
import org.example.backend.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void save(User user) {
          userRepository.save(user);
    }

    @Override
    public void deleteById(long id) {
           userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng!");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(userRole)
                .build();

        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email hoặc mật khẩu không chính xác!"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không chính xác!");
        }

        return convertToResponse(user);
    }

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .roleName(user.getRole().getName())
                .build();
    }
}
