package com.example.onlineshop.controllers;

import com.example.onlineshop.entitetes.Card;
import com.example.onlineshop.entitetes.User;
import com.example.onlineshop.security.CustomUserDetails;
import com.example.onlineshop.services.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public List<Card> getCartItems(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return cardService.getCartItemsByUser(user);
    }

    @PostMapping("/add")
    public Card addItemToCart(@RequestParam("product_Id") Long productId,
                              @RequestParam("quantity") int quantity,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return cardService.addItemToCart(productId, quantity, user);
    }
    @PutMapping("/{cartId}/quantity")
    public ResponseEntity<?> updateQuantity(@PathVariable Long cartId, @RequestParam int quantity) {

        Card updatedItem = cardService.updateQuantity(cartId, quantity);

        if (updatedItem == null) {
            return ResponseEntity.ok("Item removed from cart");
        }

        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable long id,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        cardService.deleteItemFromCart(id, user);
    }
}
