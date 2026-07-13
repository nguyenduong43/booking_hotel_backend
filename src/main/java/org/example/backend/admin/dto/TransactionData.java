package org.example.backend.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class TransactionData {
    private Long id;
    private String customerName;
    private String roomName;
    private Double amount;
    private LocalDate date;
    private String status;
}
