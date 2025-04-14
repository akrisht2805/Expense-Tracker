package com.akrisht.expenseTracker.controller;

import com.akrisht.expenseTracker.repository.UserRepository;
import com.akrisht.expenseTracker.service.ExpenseService;
import com.akrisht.expenseTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyReport(
            @RequestParam int year,
            @RequestParam int month) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email).get().getId();
        try {
            Map<String, Object> report = expenseService.getMonthlyReport(userId, year, month);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
