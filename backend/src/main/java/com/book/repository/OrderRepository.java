package com.book.repository;

import com.book.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);
    List<Order> findByBookSellerIdOrderByCreatedAtDesc(Long sellerId);

    @Query("SELECT o.book.id FROM Order o WHERE o.buyer.id = :userId")
    List<Long> findBookIdsByBuyerId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT o.buyer.id FROM Order o WHERE o.book.id IN :bookIds AND o.buyer.id <> :excludeUserId")
    List<Long> findBuyerIdsByBookIds(@Param("bookIds") List<Long> bookIds, @Param("excludeUserId") Long excludeUserId);

    @Query("SELECT DISTINCT o.book FROM Order o WHERE o.buyer.id IN :userIds AND o.book.status = 'onsale'")
    List<com.book.entity.Book> findBooksByBuyerIds(@Param("userIds") List<Long> userIds);

    @Query("SELECT SUM(o.book.price) FROM Order o WHERE o.status = :status")
    java.math.BigDecimal sumPriceByStatus(@Param("status") String status);
}