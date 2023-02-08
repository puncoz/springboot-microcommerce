package com.microcommerce.inventoryservice.service;

import com.microcommerce.inventoryservice.model.Inventory;
import com.microcommerce.inventoryservice.repository.InventoryRepository;
import com.microcommerce.inventoryservice.response.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> areInStock(List<String> skuCodes) {
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream().map(this::prepareInventoryResponse).toList();
    }

    private InventoryResponse prepareInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
