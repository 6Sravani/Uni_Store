package com.uni_store.store.Cart;

import com.uni_store.store.Product.ProductVariant;
import com.uni_store.store.Product.ProductVariantRepository;
import com.uni_store.store.User.User;
import com.uni_store.store.User.UserRepository;
import com.uni_store.store.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductVariantRepository productVariantRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public CartResponseDto getCartForUser(String userEmail) {

        User user=findUserByEmail(userEmail);
        Cart cart=getOrCreateCart(user);
        return cartMapper.toCartResponseDto(cart);
    }

    public CartResponseDto addItemToCart(String userEmail, AddToCartRequestDto requestDto) {
        User user=findUserByEmail(userEmail);
        Cart cart=getOrCreateCart(user);
        ProductVariant variant=productVariantRepository.findByIdWithProduct(requestDto.productVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));
        CartItem itemToSave = cart.addItem(variant, requestDto.quantity());

        cartItemRepository.save(itemToSave);

        return cartMapper.toCartResponseDto(cart);
    }

    public CartResponseDto removeItemFromCart(String userEmail,Long cartItemId) {

        User user=findUserByEmail(userEmail);
        Cart cart=getOrCreateCart(user);

        CartItem itemToRemove = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!itemToRemove.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("Cart item not found in user's cart");
        }
        cart.removeItem(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        return cartMapper.toCartResponseDto(cart);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserWithDetails(user)
                .orElseGet(() -> {
                    Cart newcart=Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newcart);
                });
    }
}
