package org.nerds.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nerds.dto.CreateProductDto;
import org.nerds.dto.ProductResponseDto;
import org.nerds.entities.Product;
import org.nerds.handler.exception.ConflictException;
import org.nerds.handler.exception.NoDataFoundException;
import org.nerds.repositories.ProductRepository;
import org.nerds.utils.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductResponseDto> fetchAllProducts(PageRequest pageable, String sortBy, String sortOrder) throws NoDataFoundException {
        sortOrder = Utility.extractSortOrder(sortOrder);
        log.info("sortOrder = " + sortOrder);
        Sort sort = Sort.unsorted();
        if (Utility.isStringEmptyOrNull(sortBy)) {
            sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        }
        log.info("Request sortBy = {}, sortOrder = {}", sortBy, sortOrder);
        Page<Product> products;
        long start = System.currentTimeMillis();
        products = productRepository.findAll(pageable.withSort(sort));
        log.info("Time taken to fetch the all products from repo = {} ms", (System.currentTimeMillis() - start));
        return new PageImpl<>(products.stream().map(product -> ProductResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .build()).toList(), pageable, products.getTotalElements());
    }

    public Product createProduct(CreateProductDto productDto) throws ConflictException {

        Product product = productRepository.findOneByTitle(productDto.getTitle());
        if (product != null) {
            log.warn("Duplicate product {}", product);
            throw new ConflictException(String.format("Product with title %s already exist", productDto.getTitle()));
        }
        return productRepository.save(Product.builder()
                .title(productDto.getTitle())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .build());

    }

    public ProductResponseDto fetchProductById(String id) throws NoDataFoundException {
        if(Utility.isStringEmptyOrNull(id)){
            throw new IllegalArgumentException("Provided null or empty path variable");
        }
        long start = System.currentTimeMillis();
        final Product product = productRepository.findOneById(id);
        log.info("Time taken to fetch id[{}] information {}ms", id, (System.currentTimeMillis() - start));
        if (product == null) {
            throw new NoDataFoundException("No product found with id " + id);
        }
        return ProductResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
