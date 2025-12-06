package com.book.repository;

import com.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByStatusOrderByCreatedAtDesc(String status);
    List<Book> findByStatusAndTitleContainingOrderByCreatedAtDesc(String status, String title);
    List<Book> findBySellerIdOrderByCreatedAtDesc(Long sellerId);
}