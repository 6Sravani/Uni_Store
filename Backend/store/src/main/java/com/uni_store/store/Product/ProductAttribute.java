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
@ToString(exclude = "productAttributeValues")
@Entity
@Table(name = "product_attributes")
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @NotBlank
    @Column(name = "name", nullable = false,unique = true, length = 100)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "productAttribute",cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProductAttributeValue> productAttributeValues = new LinkedHashSet<>();

    public void addProductAttributeValue(ProductAttributeValue value) {
        this.productAttributeValues.add(value);
        value.setProductAttribute(this);
    }

    public void removeProductAttributeValue(ProductAttributeValue value) {
        this.productAttributeValues.remove(value);
        value.setProductAttribute(null);
    }
}