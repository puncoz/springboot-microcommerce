package com.microcommerce.inventoryservice.controller;

import com.microcommerce.inventoryservice.response.InventoryResponse;
import com.microcommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> areInStock(@RequestParam List<String> skuCodes) {
        return inventoryService.areInStock(skuCodes);
    }
}
