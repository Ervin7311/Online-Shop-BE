package com.example.onlineshop.repositories;

import com.example.onlineshop.entitetes.Favorite;
import com.example.onlineshop.entitetes.Product;
import com.example.onlineshop.entitetes.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByProductAndUser(Product product, User user);
    Optional<Favorite> findByIdAndUser(Long id, User user);

}