package org.example.backend.booking.dto;

import lombok.*;
import org.example.backend.booking.enums.BookingStatus;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleBookingDto {
    private Long id;
    private String roomNumber;
    private String roomTypeName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalPrice;
    private BookingStatus status;
}
