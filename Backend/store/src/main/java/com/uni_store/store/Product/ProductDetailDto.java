package com.uni_store.store.Product;

import java.util.List;

public record ProductDetailDto(
        Long id,
        String name,
        String description,
        List<ImageDto> images,
        List<AttributeDto> options,
        List<VariantDto> variants
) {
}
