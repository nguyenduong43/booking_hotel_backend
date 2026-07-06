package org.example.backend.booking.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.booking.dto.BookingRequest;
import org.example.backend.booking.dto.BookingResponse;
import org.example.backend.booking.repository.IBookingRepository;
import org.example.backend.booking.service.IBookingService;
import org.example.backend.room.entity.Room;
import org.example.backend.user.dto.UserResponse;
import org.example.backend.user.entity.User;
import org.example.backend.user.service.IUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/bookings")
public class BookingController {

    private final IBookingService bookingService;
    private final IUserService userService;
    private final IBookingRepository bookingRepository;

    @GetMapping("/check-availability")
    public ResponseEntity<?> checkAvailability(
            @RequestParam Long roomTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam int quantity) {
        
        try {
            if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
                return ResponseEntity.badRequest().body(Map.of("message", "Ngày trả phòng phải sau ngày nhận phòng!"));
            }
            if (checkInDate.isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Ngày nhận phòng không được ở quá khứ!"));
            }
            
            List<Room> availableRooms = bookingRepository.findAvailableRooms(roomTypeId, checkInDate, checkOutDate);
            boolean isAvailable = availableRooms.size() >= quantity;
            
            Map<String, Object> response = new HashMap<>();
            response.put("available", isAvailable);
            response.put("availableCount", availableRooms.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request, 
                                           BindingResult bindingResult, 
                                           HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(bindingResult));
        }

        UserResponse userSession = (UserResponse) session.getAttribute("user");
        if (userSession == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Quý khách phải đăng nhập mới có thể đặt phòng!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        User user = userService.findById(userSession.getId());
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Tài khoản không tồn tại trên hệ thống!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            BookingResponse response = bookingService.createBooking(request, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
