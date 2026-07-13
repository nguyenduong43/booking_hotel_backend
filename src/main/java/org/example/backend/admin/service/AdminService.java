package org.example.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.admin.dto.DashboardStatsResponse;
import org.example.backend.admin.dto.EmployeeRequest;
import org.example.backend.admin.dto.RevenueData;
import org.example.backend.admin.dto.TransactionData;
import org.example.backend.booking.entity.Booking;
import org.example.backend.booking.enums.BookingStatus;
import org.example.backend.booking.repository.IBookingRepository;
import org.example.backend.role.Role;
import org.example.backend.role.repository.IRoleRepository;
import org.example.backend.user.entity.User;
import org.example.backend.user.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final IBookingRepository bookingRepository;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public DashboardStatsResponse getDashboardStats() {
        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> recentBookings = bookingRepository.findTop5ByOrderByCreatedAtDesc();
        
        long totalUsers = userRepository.count();
        long totalBookings = allBookings.stream()
                .map(b -> b.getBookingGroupCode() != null ? b.getBookingGroupCode() : String.valueOf(b.getId()))
                .distinct()
                .count();
        
        double totalRevenue = allBookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        // Calculate monthly revenue for the current year
        Map<Integer, Double> revenueByMonth = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            revenueByMonth.put(i, 0.0);
        }
        
        int currentYear = java.time.LocalDate.now().getYear();
        
        for (Booking b : allBookings) {
            if (b.getStatus() != BookingStatus.CANCELLED && b.getCreatedAt() != null && b.getCreatedAt().getYear() == currentYear) {
                int month = b.getCreatedAt().getMonthValue();
                revenueByMonth.put(month, revenueByMonth.get(month) + b.getTotalPrice());
            }
        }

        List<RevenueData> monthlyRevenue = revenueByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new RevenueData(
                        Month.of(e.getKey()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        e.getValue()))
                .collect(Collectors.toList());

        List<TransactionData> recentTransactions = recentBookings.stream()
                .map(b -> TransactionData.builder()
                        .id(b.getId())
                        .customerName(b.getUser().getFullName())
                        .roomName(b.getRoom().getRoomType().getName() + " - " + b.getRoom().getRoomNumber())
                        .amount(b.getTotalPrice())
                        .date(b.getCreatedAt().toLocalDate())
                        .status(b.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        return DashboardStatsResponse.builder()
                .totalRevenue(totalRevenue)
                .totalUsers(totalUsers)
                .totalBookings(totalBookings)
                .monthlyRevenue(monthlyRevenue)
                .recentTransactions(recentTransactions)
                .build();
    }

    @Transactional
    public List<User> createEmployees(List<EmployeeRequest> requests) {
        Role staffRole = roleRepository.findByName("ROLE_STAFF").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("ROLE_STAFF");
            return roleRepository.save(newRole);
        });

        List<User> newEmployees = new ArrayList<>();
        for (EmployeeRequest req : requests) {
            if (userRepository.existsByEmail(req.getEmail())) {
                continue; // Skip existing emails
            }
            User user = User.builder()
                    .fullName(req.getFullName())
                    .email(req.getEmail())
                    .password(req.getPassword() != null ? req.getPassword() : "123456")
                    .phoneNumber(req.getPhoneNumber())
                    .image(req.getImage())
                    .role(staffRole)
                    .build();
            newEmployees.add(userRepository.save(user));
        }
        return newEmployees;
    }
}
