package com.uni_store.store.Cart;

import java.math.BigDecimal;

public record CartItemResponseDto(
        Long itemId,
        Long variantId,
        String productName,
        String sku,
        BigDecimal price,
        Integer quantity,
        String imageUrl
) {}
