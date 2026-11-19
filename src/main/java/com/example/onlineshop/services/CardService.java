package com.example.onlineshop.services;

import com.example.onlineshop.entitetes.Card;
import com.example.onlineshop.entitetes.Product;
import com.example.onlineshop.entitetes.User;
import com.example.onlineshop.repositories.CardRepository;
import com.example.onlineshop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final ProductRepository productRepository;

    public List<Card> getCartItemsByUser(User user) {
        return cardRepository.findAllByUser(user);
    }

    public Card addItemToCart(Long productId, int quantity, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        Card cardItem = cardRepository.findByUserAndProduct(user, product)
                .orElse(new Card());

        cardItem.setUser(user);
        cardItem.setProduct(product);
        cardItem.setQuantity(cardItem.getId() != null ? cardItem.getQuantity() + quantity : quantity);

        return cardRepository.save(cardItem);
    }

    public void deleteItemFromCart(Long id, User user) {
        Card card = cardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        cardRepository.delete(card);
    }

    public Card updateQuantity(Long cartId, int quantity) {
        Card cartItem = cardRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cardRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(quantity);
        return cardRepository.save(cartItem);
    }
}
