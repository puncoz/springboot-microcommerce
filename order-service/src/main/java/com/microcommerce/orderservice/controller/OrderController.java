package com.microcommerce.orderservice.controller;

import com.microcommerce.orderservice.request.OrderRequest;
import com.microcommerce.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String index() {
        return "Order index page";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "inventoryFallback")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> inventoryFallback(OrderRequest orderRequest, RuntimeException exception) {
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }
}
