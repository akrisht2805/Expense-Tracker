package com.akrisht.expenseTracker.service;

import com.akrisht.expenseTracker.entity.Expense;
import com.akrisht.expenseTracker.entity.User;
import com.akrisht.expenseTracker.repository.ExpenseRepository;
import com.akrisht.expenseTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private User user;
    private Expense expense;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Test User");

        expense = new Expense();
        expense.setId(1L);
        expense.setAmount(100.0);
        expense.setCategory("Food");
        expense.setUser(user);
        expense.setDate(LocalDate.now());
    }

    @Test
    void testCreateExpense_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        Expense created = expenseService.createExpense(1L, expense);
        assertEquals(100.0, created.getAmount());
    }

    @Test
    void testCreateExpense_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> expenseService.createExpense(1L, expense));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testGetExpensesByUserId() {
        when(expenseRepository.findByUserId(1L)).thenReturn(List.of(expense));
        List<Expense> list = expenseService.getExpensesByUserId(1L);
        assertEquals(1, list.size());
    }

    @Test
    void testGetExpenseById_Success() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        Expense found = expenseService.getExpenseById(1L, 1L);
        assertEquals(100.0, found.getAmount());
    }

    @Test
    void testGetExpenseById_Unauthorized() {
        expense.getUser().setId(2L);
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        assertThrows(RuntimeException.class, () -> expenseService.getExpenseById(1L, 1L));
    }

    @Test
    void testUpdateExpense_Success() {
        Expense updated = new Expense();
        updated.setAmount(200.0);
        updated.setCategory("Travel");
        updated.setDescription("Trip");
        updated.setDate(LocalDate.now());

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(updated);

        Expense result = expenseService.updateExpense(1L, 1L, updated);
        assertEquals("Travel", result.getCategory());
    }

    @Test
    void testDeleteExpense_Success() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        expenseService.deleteExpense(1L, 1L);
        verify(expenseRepository, times(1)).delete(expense);
    }

    @Test
    void testGetTotalExpenses() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);
        when(expenseRepository.findByUserIdAndDateBetween(1L, start, end))
                .thenReturn(List.of(expense));

        Double total = expenseService.getTotalExpenses(1L, start, end);
        assertEquals(100.0, total);
    }

    @Test
    void testGetCategoryTotals() {
        when(expenseRepository.findByUserId(1L)).thenReturn(List.of(expense));

        Map<String, Double> totals = expenseService.getCategoryTotals(1L);
        assertEquals(100.0, totals.get("Food"));
    }

    @Test
    void testGetMonthlyReport() {
        when(expenseRepository.findByUserIdAndDateBetween(anyLong(), any(), any()))
                .thenReturn(List.of(expense));

        Map<String, Object> report = expenseService.getMonthlyReport(1L, 2025, 1);
        assertTrue(report.containsKey("total"));
        assertTrue(report.containsKey("categories"));
    }
}
