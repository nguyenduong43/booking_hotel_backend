package org.example.backend.booking.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private String bookingGroupCode;
    private List<SingleBookingDto> bookings;
}
