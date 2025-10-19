package com.uni_store.store.Cart;

import com.uni_store.store.Product.ProductVariant;
import com.uni_store.store.User.User;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user","cartItems"})
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,unique = true)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false,updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    // --- Helper Methods ---
    public CartItem addItem(ProductVariant variant,int quantity) {
        Optional<CartItem> excitingItemOpt = this.cartItems.stream()
                .filter(item->item.getProductVariant().getId().equals(variant.getId()))
                .findFirst();
        if(excitingItemOpt.isPresent()) {
            CartItem existingItem = excitingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity()+quantity);
            return existingItem;
        }
        else {
            CartItem newCartItem= CartItem.builder()
                    .cart(this)
                    .productVariant(variant)
                    .quantity(quantity)
                    .build();
            this.cartItems.add(newCartItem);
            return newCartItem;
        }
    }

    public void removeItem(CartItem item) {
        this.cartItems.remove(item);
        item.setCart(null);
    }
}