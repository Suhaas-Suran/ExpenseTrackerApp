package com.expensetracker.service;

import com.expensetracker.dto.TransactionDTOs.*;
import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;
import com.expensetracker.model.User;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionResponse createTransaction(Long userId, TransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setDate(request.getDate());
        transaction.setNotes(request.getNotes());

        transaction = transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    public List<TransactionResponse> getAllTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByDateDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByType(Long userId, TransactionType type) {
        return transactionRepository.findByUserIdAndTypeOrderByDateDesc(userId, type)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByCategory(Long userId, Category category) {
        return transactionRepository.findByUserIdAndCategoryOrderByDateDesc(userId, category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MonthlySummary getMonthlySummary(Long userId, Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        BigDecimal totalIncome = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, TransactionType.INCOME, startDate, endDate);

        BigDecimal totalExpense = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, TransactionType.EXPENSE, startDate, endDate);

        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpense == null) totalExpense = BigDecimal.ZERO;

        BigDecimal netSavings = totalIncome.subtract(totalExpense);

        List<Object[]> categoryData = transactionRepository.sumByCategoryAndUserIdAndTypeAndDateBetween(
                userId, TransactionType.EXPENSE, startDate, endDate);

        List<CategoryBreakdown> breakdown = categoryData.stream()
                .map(row -> new CategoryBreakdown((Category) row[0], (BigDecimal) row[1]))
                .collect(Collectors.toList());

        return new MonthlySummary(totalIncome, totalExpense, netSavings, breakdown);
    }

    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        transactionRepository.delete(transaction);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getDate(),
                transaction.getNotes(),
                transaction.getCreatedAt()
        );
    }
}