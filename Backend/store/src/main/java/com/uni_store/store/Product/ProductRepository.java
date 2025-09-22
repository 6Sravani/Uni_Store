package com.uni_store.store.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.productVariants " +
            "LEFT JOIN FETCH p.productImages",
            countQuery = "SELECT COUNT(p) FROM Product p")
    Page<Product> findAllWithDetails(Pageable pageable);

    Optional<Product> findBySlug(String slug);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.productVariants pv " +
            "LEFT JOIN FETCH pv.attributeValues av " +
            "LEFT JOIN FETCH av.productAttribute " +
            "LEFT JOIN FETCH p.productImages " +
            "WHERE p.slug = :slug")
    Optional<Product> findBySlugWithDetails(@Param("slug") String slug);
}
