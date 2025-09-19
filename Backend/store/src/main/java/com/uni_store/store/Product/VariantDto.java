package com.uni_store.store.Product;

import java.math.BigDecimal;
import java.util.Map;

public record VariantDto(
        Long id,
        String sku,
        BigDecimal price,
        int quantity,
        // e.g., {"Size": "M", "Color": "Blue"}
        Map<String, String> attributes
) {
}
