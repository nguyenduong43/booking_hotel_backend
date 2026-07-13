package org.example.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.admin.dto.DashboardStatsResponse;
import org.example.backend.admin.dto.EmployeeRequest;
import org.example.backend.admin.service.AdminService;
import org.example.backend.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @PostMapping("/employees/batch")
    public ResponseEntity<List<User>> createEmployeesBatch(@RequestBody List<EmployeeRequest> requests) {
        return ResponseEntity.ok(adminService.createEmployees(requests));
    }
}
