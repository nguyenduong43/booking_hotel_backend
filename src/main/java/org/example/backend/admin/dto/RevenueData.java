package org.example.backend.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueData {
    private String month;
    private Double revenue;
}
