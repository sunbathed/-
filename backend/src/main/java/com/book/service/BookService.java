package com.book.service;

import com.book.entity.Book;
import com.book.repository.BookRepository;
import com.book.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository repo;
    private final OrderRepository orderRepo;

    public BookService(BookRepository repo, OrderRepository orderRepo) {
        this.repo = repo;
        this.orderRepo = orderRepo;
    }

    public List<Book> recommend(Long userId) {
        List<Book> recommendations = new ArrayList<>();
        List<Long> myBookIds = new ArrayList<>();

        if (userId != null) {
            // 1. Get books I bought
            myBookIds = orderRepo.findBookIdsByBuyerId(userId);

            if (!myBookIds.isEmpty()) {
                // 2. Find users who bought same books
                List<Long> similarUserIds = orderRepo.findBuyerIdsByBookIds(myBookIds, userId);

                if (!similarUserIds.isEmpty()) {
                    // 3. Get books they bought
                    List<Book> booksFromOthers = orderRepo.findBooksByBuyerIds(similarUserIds);

                    // 4. Filter
                    for (Book b : booksFromOthers) {
                        if (!myBookIds.contains(b.getId()) && // Not bought by me
                            !b.getSeller().getId().equals(userId)) { // Not sold by me
                            recommendations.add(b);
                        }
                    }
                }
            }
        }

        // 5. Fallback / Fill up to 6 (for UI display)
        if (recommendations.size() < 6) {
            List<Book> latest = listOnsale();
            for (Book b : latest) {
                if (recommendations.size() >= 6) break;
                
                // Check duplicates
                boolean exists = false;
                for (Book r : recommendations) {
                    if (r.getId().equals(b.getId())) {
                        exists = true;
                        break;
                    }
                }
                
                // Also exclude my own books and bought books if logged in
                if (!exists) {
                    if (userId != null) {
                         if (b.getSeller().getId().equals(userId)) continue;
                         if (myBookIds.contains(b.getId())) continue;
                    }
                    recommendations.add(b);
                }
            }
        }

        return recommendations.stream().limit(6).collect(Collectors.toList());
    }

    public List<Book> listOnsale() {
        return repo.findByStatusOrderByCreatedAtDesc("onsale");
    }

    public List<Book> search(String keyword) {
        return repo.findByStatusAndTitleContainingOrderByCreatedAtDesc("onsale", keyword);
    }

    public List<Book> listPending() {
        return repo.findByStatusOrderByCreatedAtDesc("pending");
    }

    public List<Book> listBySeller(Long sellerId) {
        return repo.findBySellerIdOrderByCreatedAtDesc(sellerId);
    }

    public Optional<Book> findById(Long id) {
        return repo.findById(id);
    }

    public Book save(Book book) {
        return repo.save(book);
    }
}