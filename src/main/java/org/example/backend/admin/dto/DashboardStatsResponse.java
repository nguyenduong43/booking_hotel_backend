package org.example.backend.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardStatsResponse {
    private Double totalRevenue;
    private Long totalUsers;
    private Long totalBookings;
    private List<RevenueData> monthlyRevenue;
    private List<TransactionData> recentTransactions;
}
