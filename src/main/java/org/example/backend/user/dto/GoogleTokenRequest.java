package org.example.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleTokenRequest {
    @NotBlank(message = "Token không được để trống")
    private String token;
}
