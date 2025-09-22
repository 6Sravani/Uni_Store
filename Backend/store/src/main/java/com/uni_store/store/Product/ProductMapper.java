package com.uni_store.store.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {VariantMapper.class, ImageMapper.class}
)
public interface ProductMapper {


    CategoryDto toCategoryDto(ProductCategory productCategory);
    List<CategoryDto> toCategoryDtoList(List<CategoryDto> productCategories);


    @Mapping(target = "mainImageUrl", expression = "java(mapMainImage(product))")
    @Mapping(target = "price", expression = "java(mapDefaultPrice(product))")
    ProductSummaryDto toProductSummaryDto(Product product);

    default String mapMainImage(Product product) {
        if (product == null) return null;

        Optional<ProductVariant> lowestPriceVariant = findLowestPriceVariant(product);

        if (lowestPriceVariant.isPresent() && lowestPriceVariant.get().getImageUrl() != null && !lowestPriceVariant.get().getImageUrl().isBlank()) {
            return lowestPriceVariant.get().getImageUrl();
        }

        if (product.getProductImages() != null) {
            return product.getProductImages().stream()
                    .min(Comparator.comparing(ProductImage::getSortOrder))
                    .map(ProductImage::getImageUrl)
                    .orElse(null);
        }

        return null;
    }

    default BigDecimal mapDefaultPrice(Product product) {
        return findLowestPriceVariant(product)
                .map(ProductVariant::getPrice)
                .orElse(null);
    }


    @Mapping(target = "variants", source = "productVariants")
    @Mapping(target = "options", source = "productVariants")
    ProductDetailDto toProductDetailDto(Product product);

    default List<AttributeDto> mapOptions(Set<ProductVariant> variants) {
        if (variants == null || variants.isEmpty()) {
            return List.of();
        }
        Map<String, Set<String>> attributesMap = variants.stream()
                .flatMap(variant -> variant.getAttributeValues().stream())
                .collect(Collectors.groupingBy(
                        attributeValue -> attributeValue.getProductAttribute().getName(),
                        Collectors.mapping(ProductAttributeValue::getValue, Collectors.toSet())
                ));
        return attributesMap.entrySet().stream()
                .map(entry -> new AttributeDto(entry.getKey(), List.copyOf(entry.getValue())))
                .collect(Collectors.toList());
    }


    private Optional<ProductVariant> findLowestPriceVariant(Product product) {
        if (product == null || product.getProductVariants() == null || product.getProductVariants().isEmpty()) {
            return Optional.empty();
        }
        return product.getProductVariants().stream()
                .min(Comparator.comparing(ProductVariant::getPrice));
    }
}