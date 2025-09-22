package com.uni_store.store.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VariantMapper {
    // 1. Add a @Mapping annotation to specify the source for the 'attributes' field.
    @Mapping(target = "attributes", expression = "java(mapAttributes(variant.getAttributeValues()))")
    VariantDto toVariantDto(ProductVariant variant);

    // 2. Provide a custom method to perform the complex conversion.
    // MapStruct will automatically find and use this method because its input and output
    // types match what the @Mapping annotation requires.
    default Map<String, String> mapAttributes(Set<ProductAttributeValue> attributeValues) {
        if (attributeValues == null || attributeValues.isEmpty()) {
            return Collections.emptyMap(); // Return an empty map instead of null for safety
        }

        // This uses a Java Stream to convert the Set of objects into a Map.
        return attributeValues.stream()
                .collect(Collectors.toMap(
                        // The key for the map is the attribute's name (e.g., "Color")
                        value -> value.getProductAttribute().getName(),
                        // The value for the map is the attribute's value (e.g., "Blue")
                        ProductAttributeValue::getValue
                ));
    }
}
