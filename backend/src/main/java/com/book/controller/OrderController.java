package com.book.controller;

import com.book.entity.Order;
import com.book.service.BookService;
import com.book.service.OrderService;
import com.book.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import com.book.utils.Maps;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderSvc;
    private final BookService bookSvc;
    private final UserService userSvc;
    public OrderController(OrderService orderSvc, BookService bookSvc, UserService userSvc) {
        this.orderSvc = orderSvc; this.bookSvc = bookSvc; this.userSvc = userSvc;
    }

    @PostMapping("/orders")
    public Map<String,Long> create(@RequestBody Map<String,Object> body) {
        Long bookId = Long.valueOf(body.get("book_id").toString());
        Long buyerId = Long.valueOf(body.get("buyer_id").toString());
        Order o = orderSvc.create(
                bookSvc.findById(bookId).orElseThrow(() -> new RuntimeException("图书不存在")),
                userSvc.findById(buyerId)
        );
        return Maps.of("id", o.getId());
    }

    @GetMapping("/orders/{userId}")
    public List<Order> list(@PathVariable("userId") Long userId) {
        return orderSvc.listByBuyer(userId);
    }

    @GetMapping("/orders/seller/{sellerId}")
    public List<Order> listBySeller(@PathVariable("sellerId") Long sellerId) {
        return orderSvc.listBySeller(sellerId);
    }

    @PostMapping("/orders/{id}/complete")
    public void complete(@PathVariable("id") Long id, @RequestBody Map<String,Long> body) {
        orderSvc.complete(id, body.get("user_id"));
    }
}