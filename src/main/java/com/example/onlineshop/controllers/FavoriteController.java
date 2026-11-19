package com.example.onlineshop.controllers;

import com.example.onlineshop.entitetes.Favorite;
import com.example.onlineshop.entitetes.User;
import com.example.onlineshop.security.CustomUserDetails;
import com.example.onlineshop.services.FavoriteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/favorite")
public class FavoriteController {


    private final FavoriteService favoriteService;

    @GetMapping
    public List<Favorite> getFavorites(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return favoriteService.getFavoritesByUser(user);
    }

    @PostMapping("/add")
    public Favorite addFavorite(@RequestParam("product_Id") long productId,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return favoriteService.addFavorite(productId, user);
    }

    @DeleteMapping("/{id}")
    public void deleteFavorite(@PathVariable long id,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        favoriteService.removeFavorite(id, user);
    }
}
