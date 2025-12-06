package com.book.controller;

import com.book.entity.Book;
import com.book.entity.Report;
import com.book.entity.User;
import com.book.repository.BookRepository;
import com.book.repository.ReportRepository;
import com.book.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.book.utils.Maps;

@RestController
@RequestMapping("/api")
public class ReportController {
    private final ReportRepository repo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public ReportController(ReportRepository repo, BookRepository bookRepo, UserRepository userRepo) {
        this.repo = repo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/reports")
    public Map<String,Long> create(@RequestBody Map<String,Object> body) {
        String type = (String) body.get("type");
        Long reporterId = Long.valueOf(body.get("reporter_id").toString());
        String reason = (String) body.get("reason");

        Report r = new Report();
        r.setType(type);
        r.setReporter(userRepo.findById(reporterId).orElseThrow(() -> new RuntimeException("用户不存在")));
        r.setReason(reason);

        if ("book".equals(type)) {
            Long bookId = Long.valueOf(body.get("book_id").toString());
            Book b = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("图书不存在"));
            r.setBook(b);
        } else if ("user".equals(type)) {
            Long targetUserId = Long.valueOf(body.get("target_user_id").toString());
            User u = userRepo.findById(targetUserId).orElseThrow(() -> new RuntimeException("用户不存在"));
            r.setTargetUser(u);
        }
        Report saved = repo.save(r);
        return Maps.of("id", saved.getId());
    }

    @GetMapping("/reports/pending")
    public List<Report> listPending() {
        return repo.findByStatusOrderByCreatedAtDesc("pending");
    }

    @PostMapping("/reports/{id}/resolve")
    public void resolve(@PathVariable("id") Long id, @RequestBody Map<String,String> body) {
        String action = body.getOrDefault("action", "invalid");
        Report r = repo.findById(id).orElseThrow(() -> new RuntimeException("举报不存在"));
        if ("valid".equals(action)) {
            r.setStatus("valid");
            // 举报有效时，自动下架书籍
            if ("book".equals(r.getType()) && r.getBook() != null) {
                Book b = r.getBook();
                b.setStatus("rejected"); // 标记为下架/驳回状态
                bookRepo.save(b);
            }
        } else {
            r.setStatus("invalid");
        }
        repo.save(r);
    }
}

