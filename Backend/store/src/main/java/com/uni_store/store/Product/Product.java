package com.uni_store.store.Product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"productCategories", "productImages", "productVariants"})
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @NotBlank
    @Column(name = "slug", nullable = false,unique = true)
    private String slug;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_category_assignments",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private Set<ProductCategory> productCategories = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<ProductImage> productImages = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<ProductVariant> productVariants = new LinkedHashSet<>();



    public void addCategory(ProductCategory category) {
        this.productCategories.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(ProductCategory category) {
        this.productCategories.remove(category);
        category.getProducts().remove(this);
    }

    public void addProductImage(ProductImage image) {
        this.productImages.add(image);
        image.setProduct(this);
    }

    public void removeProductImage(ProductImage image) {
        this.productImages.remove(image);
        image.setProduct(null);
    }

    public void addProductVariant(ProductVariant variant) {
        this.productVariants.add(variant);
        variant.setProduct(this);
    }

    public void removeProductVariant(ProductVariant variant) {
        this.productVariants.remove(variant);
        variant.setProduct(null);
    }
}