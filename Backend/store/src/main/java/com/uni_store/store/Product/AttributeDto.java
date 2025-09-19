package com.uni_store.store.Product;

import java.util.List;

public record AttributeDto(
        String name,
        List<String> values
) {}
