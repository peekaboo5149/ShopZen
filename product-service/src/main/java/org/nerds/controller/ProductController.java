package org.nerds.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nerds.dto.CreateProductDto;
import org.nerds.dto.ProductResponseDto;
import org.nerds.handler.exception.ConflictException;
import org.nerds.handler.exception.NoDataFoundException;
import org.nerds.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder
    ) throws NoDataFoundException {
        PageRequest pageRequest = PageRequest.of(offset, limit);
        Page<ProductResponseDto> products = productService.fetchAllProducts(pageRequest, sortBy, sortOrder);
        if (products.isEmpty()) {
            throw new NoDataFoundException();
        }
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductDto productRequest) throws ConflictException {
        var product = productService.createProduct(productRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductByName(@PathVariable("id") String id) throws NoDataFoundException {

        return ResponseEntity.ok(productService.fetchProductById(id));
    }

}
