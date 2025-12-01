package com.uni_store.store.Cart;

import com.uni_store.store.User.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDto> getUserCart(Authentication authentication){

        String userEmail = authentication.getName();
        CartResponseDto cartDto = cartService.getCartForUser(userEmail);

        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDto> addItemToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequestDto requestDto) {

        String userEmail=authentication.getName();
        CartResponseDto updatedCart = cartService.addItemToCart(userEmail, requestDto);

        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDto> removeItemFromCart(
            Authentication authentication,
            @PathVariable Long itemId) {

        String userEmail=authentication.getName();
        CartResponseDto updatedCart = cartService.removeItemFromCart(userEmail, itemId);

        return ResponseEntity.ok(updatedCart);
    }
}
