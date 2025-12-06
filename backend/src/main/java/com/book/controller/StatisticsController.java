package com.book.controller;

import com.book.repository.BookRepository;
import com.book.repository.OrderRepository;
import com.book.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin")
public class StatisticsController {

    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final OrderRepository orderRepo;

    public StatisticsController(UserRepository userRepo, BookRepository bookRepo, OrderRepository orderRepo) {
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
        this.orderRepo = orderRepo;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        long userCount = userRepo.count();
        long bookCount = bookRepo.count();
        long orderCount = orderRepo.count();
        BigDecimal totalAmount = orderRepo.sumPriceByStatus("completed");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userCount);
        stats.put("bookCount", bookCount);
        stats.put("orderCount", orderCount);
        stats.put("totalAmount", totalAmount != null ? totalAmount : BigDecimal.ZERO);
        
        return stats;
    }
}
