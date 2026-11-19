package com.example.onlineshop.repositories;

import com.example.onlineshop.entitetes.Card;
import com.example.onlineshop.entitetes.Product;
import com.example.onlineshop.entitetes.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByUserAndProduct(User user, Product product);
    List<Card> findAllByUser(User user);
    Optional<Card> findById(Long Id);
    Optional<Card> findByIdAndUser(Long id, User user);
    List<Card> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
