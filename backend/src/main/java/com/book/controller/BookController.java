package com.book.controller;

import com.book.entity.Book;
import com.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.book.utils.Maps;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService svc;
    public BookController(BookService svc) { this.svc = svc; }

    @GetMapping("/books")
    public List<Book> list(@RequestParam(value = "q", required = false) String q) {
        if (q != null && !q.trim().isEmpty()) {
            return svc.search(q);
        }
        return svc.listOnsale();
    }

    @GetMapping("/books/my")
    public List<Book> listMyBooks(@RequestParam("sellerId") Long sellerId) {
        return svc.listBySeller(sellerId);
    }

    @GetMapping("/books/pending")
    public List<Book> listPending() {
        return svc.listPending();
    }

    @GetMapping("/books/recommend")
    public List<Book> recommend(@RequestParam(value = "userId", required = false) Long userId) {
        return svc.recommend(userId);
    }

    @PostMapping("/books/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable("id") Long id) {
        try {
            Book b = svc.findById(id).orElseThrow(() -> new RuntimeException("图书不存在"));
            if (!"pending".equals(b.getStatus())) {
                return ResponseEntity.badRequest().body(Maps.of("message", "当前状态不可审核"));
            }
            b.setStatus("onsale");
            svc.save(b);
            return ResponseEntity.ok(Maps.of("status", "ok", "id", b.getId(), "newStatus", b.getStatus()));
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                msg = "数据不完整：" + ((org.springframework.dao.DataIntegrityViolationException) e).getMostSpecificCause().getMessage();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Maps.of("message", msg != null ? msg : "未知错误"));
        }
    }

    @PostMapping("/books/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable("id") Long id) {
        try {
            Book b = svc.findById(id).orElseThrow(() -> new RuntimeException("图书不存在"));
            if (!"pending".equals(b.getStatus())) {
                return ResponseEntity.badRequest().body(Maps.of("message", "当前状态不可审核"));
            }
            b.setStatus("rejected");
            svc.save(b);
            return ResponseEntity.ok(Maps.of("status", "ok", "id", b.getId(), "newStatus", b.getStatus()));
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                msg = "数据不完整：" + ((org.springframework.dao.DataIntegrityViolationException) e).getMostSpecificCause().getMessage();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Maps.of("message", msg != null ? msg : "未知错误"));
        }
    }

    @GetMapping("/books/{id}")
    public Book get(@PathVariable("id") Long id) {
        System.out.println("Requesting book id: " + id);
        try {
            Book b = svc.findById(id).orElseThrow(() -> new RuntimeException("图书不存在"));
            System.out.println("Found book: " + b.getTitle());
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/books")
    public Map<String,Long> create(@RequestBody Book body) {
        Book b = svc.save(body);
        return Maps.of("id", b.getId());
    }
}
