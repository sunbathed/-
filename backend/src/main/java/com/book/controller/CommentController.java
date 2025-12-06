package com.book.controller;

import com.book.entity.Book;
import com.book.entity.Comment;
import com.book.entity.User;
import com.book.repository.BookRepository;
import com.book.repository.CommentRepository;
import com.book.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentRepository repo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public CommentController(CommentRepository repo, BookRepository bookRepo, UserRepository userRepo) {
        this.repo = repo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/books/{bookId}/comments")
    public List<Comment> listByBook(@PathVariable("bookId") Long bookId) {
        return repo.findByBookIdOrderByCreatedAtDesc(bookId);
    }

    @PostMapping("/comments")
    public Comment create(@RequestBody Map<String, Object> body) {
        Long bookId = Long.valueOf(body.get("book_id").toString());
        Long userId = Long.valueOf(body.get("user_id").toString());
        String content = (String) body.get("content");

        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("图书不存在"));
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));

        Comment c = new Comment();
        c.setBook(book);
        c.setUser(user);
        c.setContent(content);
        return repo.save(c);
    }
}
