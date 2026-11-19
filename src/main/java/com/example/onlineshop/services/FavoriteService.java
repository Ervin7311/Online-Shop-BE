package com.example.onlineshop.services;

import com.example.onlineshop.entitetes.Favorite;
import com.example.onlineshop.entitetes.Product;
import com.example.onlineshop.entitetes.User;
import com.example.onlineshop.repositories.FavoriteRepository;
import com.example.onlineshop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final ProductRepository productRepository;

    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUser(user);
    }

    public Favorite addFavorite(Long productId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Favorite favorite = favoriteRepository.findByProductAndUser(product, user)
                .orElse(new Favorite());

        favorite.setUser(user);
        favorite.setProduct(product);

        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long id, User user) {
        Favorite favorite = favoriteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }
}