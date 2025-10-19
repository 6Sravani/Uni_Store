package com.uni_store.store.Cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "itemId",source = "id")
    @Mapping(target = "variantId",source = "productVariant.id")
    @Mapping(target = "productName",source = "productVariant.product.name")
    @Mapping(target = "sku",source = "productVariant.sku")
    @Mapping(target = "price",source = "productVariant.price")
    @Mapping(target = "quantity",source = "quantity")
    @Mapping(target = "imageUrl",source = "productVariant.productVImages")
    CartItemResponseDto ToCartItemResponseDto(CartItem cartItem);

    // Helper method to map a Set of items to a List of DTOs
    List<CartItemResponseDto> toCartItemResponseDtoList(Set<CartItem> items);

    @Mapping(target = "totalPrice",source = "cartItems",qualifiedByName = "calculateTotalPrice")
    @Mapping(target = "totalItems",source = "cartItems",qualifiedByName = "calculateTotalItems")
    CartResponseDto ToCartResponseDto(Cart cart);

    @Named("calculateTotalPrice")
    default BigDecimal calculateTotalPrice(Set<CartItem> cartItems) {
        if(cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return cartItems.stream()
                .map(cartItem->cartItem.getProductVariant().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("calculateTotalItems")
    default Integer calculateTotalItems(Set<CartItem> cartItems) {
        if(cartItems == null || cartItems.isEmpty()) {
            return 0;
        }
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
