package com.expensetracker.controller;

import com.expensetracker.dto.TransactionDTOs.*;
import com.expensetracker.model.Category;
import com.expensetracker.model.TransactionType;
import com.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            Authentication authentication,
            @Valid @RequestBody TransactionRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.createTransaction(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getAllTransactions(userId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByType(
            Authentication authentication,
            @PathVariable TransactionType type) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getTransactionsByType(userId, type));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCategory(
            Authentication authentication,
            @PathVariable Category category) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getTransactionsByCategory(userId, category));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDateRange(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(userId, startDate, endDate));
    }

    @GetMapping("/summary/{year}/{month}")
    public ResponseEntity<MonthlySummary> getMonthlySummary(
            Authentication authentication,
            @PathVariable Integer year,
            @PathVariable Integer month) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getMonthlySummary(userId, year, month));
    }

    @GetMapping("/summary/current")
    public ResponseEntity<MonthlySummary> getCurrentMonthSummary(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        YearMonth current = YearMonth.now();
        return ResponseEntity.ok(transactionService.getMonthlySummary(userId, current.getYear(), current.getMonthValue()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = (Long) authentication.getPrincipal();
        transactionService.deleteTransaction(userId, id);
        return ResponseEntity.noContent().build();
    }
}