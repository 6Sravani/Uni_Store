package com.uni_store.store.Cart;

import com.uni_store.store.Product.ProductVariant;
import com.uni_store.store.Product.ProductVariantRepository;
import com.uni_store.store.User.User;
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

    public CartResponseDto getCartForUser(User user) {

        Cart cart=getOrCreateCart(user);
        return cartMapper.ToCartResponseDto(cart);
    }

    public CartResponseDto addItemToCart(User user, AddToCartRequestDto requestDto) {

        Cart cart=getOrCreateCart(user);
        ProductVariant variant=productVariantRepository.findById(requestDto.productVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));
        CartItem itemToSave = cart.addItem(variant, requestDto.quantity());

        cartItemRepository.save(itemToSave);

        return getCartForUser(user);
    }

    public CartResponseDto removeItemFromCart(User user,Long cartItemId) {
        Cart cart=getOrCreateCart(user);

        CartItem itemToRemove = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!itemToRemove.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("Cart item not found in user's cart");
        }
        cart.removeItem(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        return cartMapper.ToCartResponseDto(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newcart=Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newcart);
                });
    }
}
