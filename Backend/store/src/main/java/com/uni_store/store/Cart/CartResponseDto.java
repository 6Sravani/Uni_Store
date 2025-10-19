package com.uni_store.store.Cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDto (
        List<CartItemResponseDto> cartItems,
        BigDecimal totalPrice,
        int totalItems
){}