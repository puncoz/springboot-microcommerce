package com.microcommerce.productservice.controller;

import com.microcommerce.productservice.request.ProductCreateRequest;
import com.microcommerce.productservice.response.ProductResponse;
import com.microcommerce.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> index() {
        return productService.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductCreateRequest request) {
        productService.createProduct(request);

    }
}
