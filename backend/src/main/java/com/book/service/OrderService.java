package com.book.service;

import com.book.entity.Book;
import com.book.entity.Order;
import com.book.entity.User;
import com.book.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repo;
    private final BookService bookService;
    public OrderService(OrderRepository repo, BookService bookService) {
        this.repo = repo;
        this.bookService = bookService;
    }

    @Transactional
    public Order create(Book book, User buyer) {
        if (!"onsale".equals(book.getStatus())) throw new RuntimeException("图书不可售");
        Order o = new Order();
        o.setBook(book);
        o.setBuyer(buyer);
        book.setStatus("sold");
        bookService.save(book);
        return repo.save(o);
    }

    public List<Order> listByBuyer(Long buyerId) {
        return repo.findByBuyerIdOrderByCreatedAtDesc(buyerId);
    }

    public List<Order> listBySeller(Long sellerId) {
        return repo.findByBookSellerIdOrderByCreatedAtDesc(sellerId);
    }

    public void complete(Long orderId, Long userId) {
        Order o = repo.findById(orderId).orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!o.getBuyer().getId().equals(userId)) throw new RuntimeException("无权操作");
        o.setStatus("completed");
        repo.save(o);
    }
}