package com.expensetracker.dto;

import com.expensetracker.model.Category;
import com.expensetracker.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionRequest {
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotNull(message = "Type is required")
        private TransactionType type;

        @NotNull(message = "Category is required")
        private Category category;

        @NotNull(message = "Date is required")
        private LocalDate date;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionResponse {
        private Long id;
        private BigDecimal amount;
        private TransactionType type;
        private Category category;
        private LocalDate date;
        private String notes;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBreakdown {
        private Category category;
        private BigDecimal totalAmount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlySummary {
        private BigDecimal totalIncome;
        private BigDecimal totalExpense;
        private BigDecimal netSavings;
        private java.util.List<CategoryBreakdown> expenseBreakdown;
    }
}