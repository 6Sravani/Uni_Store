package com.uni_store.store.Cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddToCartRequestDto (
        @NotNull
        Long productVariantId,

        @NotNull
        @Positive(message = "Quantity must be at least 1")
        Integer quantity
){}
