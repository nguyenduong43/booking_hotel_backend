package org.example.backend.booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    
    @NotNull(message = "ID loại phòng không được để trống")
    private Long roomTypeId;

    @NotBlank(message = "Tên khách hàng không được để trống")
    private String customerName;

    @NotBlank(message = "Email khách hàng không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String customerEmail;

    @NotBlank(message = "Số điện thoại khách hàng không được để trống")
    private String customerPhone;

    @NotNull(message = "Ngày nhận phòng không được để trống")
    private LocalDate checkInDate;

    @NotNull(message = "Ngày trả phòng không được để trống")
    private LocalDate checkOutDate;

    @NotNull(message = "Số lượng khách không được để trống")
    @Min(value = 1, message = "Số lượng khách phải lớn hơn hoặc bằng 1")
    private Integer numberOfGuests;

    @NotNull(message = "Số lượng phòng đặt không được để trống")
    @Min(value = 1, message = "Số lượng phòng phải lớn hơn hoặc bằng 1")
    private Integer quantity;
}
