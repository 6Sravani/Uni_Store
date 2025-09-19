package com.uni_store.store.Product;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDto ToImageDto(ProductImage image);
}
