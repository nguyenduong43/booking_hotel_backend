package org.example.backend.booking.service;

import org.example.backend.booking.dto.BookingRequest;
import org.example.backend.booking.dto.BookingResponse;
import org.example.backend.user.entity.User;

public interface IBookingService {
    BookingResponse createBooking(BookingRequest request, User user);
}
