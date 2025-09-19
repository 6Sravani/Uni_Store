package com.uni_store.store.Product;

import java.util.List;

public record CategoryDto(
        Long id,
        String name,
        String slug,
        List<CategoryDto> children
) { }