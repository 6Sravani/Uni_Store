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
@ToString(exclude = {"parentCategory","productCategories","products"})
@Entity
@Table(name = "product_categories")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @NotBlank
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 255)
    @NotBlank
    @Column(name = "slug", nullable = false,unique = true)
    private String slug;

    @Lob
    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private ProductCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private Set<ProductCategory> productCategories = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "productCategories")
    @Builder.Default
    private Set<Product> products = new LinkedHashSet<>();


    public  void addProductCategory(ProductCategory productCategory){
        this.productCategories.add(productCategory);
        productCategory.setParentCategory(this);
    }

    public void removeProductCategory(ProductCategory productCategory){
        this.productCategories.remove(productCategory);
        productCategory.setParentCategory(null);
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.getProductCategories().add(this);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
        product.getProductCategories().remove(this);
    }
}