package com.uni_store.store.Product;

import com.uni_store.store.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository CategoryRepository;
    private final ProductMapper productMapper;

    public List<CategoryDto> getAllCategories() {
        List<ProductCategory> allCategories = CategoryRepository.findAll();

        Map<Long, CategoryDto> dtoMap = allCategories.stream()
                .collect(Collectors.toMap(
                        ProductCategory::getId,
                        productMapper::toCategoryDto
                ));

        List<CategoryDto> topLevelCategories = new ArrayList<>();
        allCategories.forEach(category -> {
            if(category.getParentCategory() == null) {
                topLevelCategories.add(dtoMap.get(category.getId()));
            }else{
                CategoryDto parentDto=dtoMap.get(category.getParentCategory().getId());
                if(parentDto!=null) {
                    parentDto.children().add(dtoMap.get(category.getId()));
                }
            }
        });
        return topLevelCategories;
    }

    public Page<ProductSummaryDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage=productRepository.findAll(pageable);
        return productPage.map(productMapper::toProductSummaryDto);
    }

    public ProductDetailDto getProductBySlug(String slug) {
        Product product=productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        return productMapper.toProductDetailDto(product);
    }
}
