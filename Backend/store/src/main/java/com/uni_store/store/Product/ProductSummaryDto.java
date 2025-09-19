package com.uni_store.store.Product;

import java.math.BigDecimal;

public record ProductSummaryDto(
        Long id,
        String name,
        String slug,
        BigDecimal price,
        String mainImageUrl
) {
}
