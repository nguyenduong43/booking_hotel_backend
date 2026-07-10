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
import java.util.Collections;
import java.util.UUID;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.example.backend.user.dto.GoogleTokenRequest;

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

    @Override
    @Transactional
    public UserResponse loginWithGoogle(GoogleTokenRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList("441238238424-badhdniaj9o7v0b7jifam05kaong9aje.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getToken());
            if (idToken == null) {
                throw new IllegalArgumentException("Token không hợp lệ!");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                // Tạo user mới
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
                
                user = User.builder()
                        .email(email)
                        .password(UUID.randomUUID().toString()) // Mật khẩu ngẫu nhiên cho user Google
                        .fullName(name)
                        .role(userRole)
                        .build();
                user = userRepository.save(user);
            }

            return convertToResponse(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Đăng nhập Google thất bại: " + e.getMessage());
        }
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
