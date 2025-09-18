package com.uni_store.store.Product;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"product", "productAttributeValues", "productVImages"})
@Entity
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Size(max = 100)
    @NotBlank
    @Column(name = "sku", nullable = false, length = 100)
    private String sku;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.0")
    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @NotNull
    @Column(name = "quantity",nullable = false )
    @Builder.Default
    private Integer quantity = 0;

    @Size(max = 512)
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false,updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(
            mappedBy = "productVariant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<ProductImage> productVImages = new LinkedHashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_variant_attributes",
            joinColumns = @JoinColumn(name = "product_variant_id"),
            inverseJoinColumns = @JoinColumn(name = "product_attribute_value_id")
    )
    @Builder.Default
    private Set<ProductAttributeValue> productAttributeValues = new LinkedHashSet<>();


    public void addAttributeValue(ProductAttributeValue value) {
        this.productAttributeValues.add(value);
    }

    public void removeAttributeValue(ProductAttributeValue value) {
        this.productAttributeValues.remove(value);
    }

    public void addProductImage(ProductImage image) {
        this.productVImages.add(image);
        image.setProductVariant(this);
    }
    public void removeProductImage(ProductImage image) {
        this.productVImages.remove(image);
        image.setProductVariant(null);
    }
}