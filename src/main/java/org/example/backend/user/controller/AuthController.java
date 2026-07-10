package org.example.backend.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.user.dto.LoginRequest;
import org.example.backend.user.dto.RegisterRequest;
import org.example.backend.user.dto.UserResponse;
import org.example.backend.user.dto.GoogleTokenRequest;
import org.example.backend.user.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(bindingResult));
        }
        try {
            UserResponse response = userService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(bindingResult));
        }
        try {
            UserResponse response = userService.login(request);
            session.setAttribute("user", response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@Valid @RequestBody GoogleTokenRequest request, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(bindingResult));
        }
        try {
            UserResponse response = userService.loginWithGoogle(request);
            session.setAttribute("user", response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(HttpSession session) {
        UserResponse response = (UserResponse) session.getAttribute("user");
        if (response == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Chưa đăng nhập");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        return ResponseEntity.ok(response);
    }

    private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
