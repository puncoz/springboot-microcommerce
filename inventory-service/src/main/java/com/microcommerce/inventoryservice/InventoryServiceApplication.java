package com.microcommerce.inventoryservice;

import com.microcommerce.inventoryservice.model.Inventory;
import com.microcommerce.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory inventory = new Inventory();
            inventory.setSkuCode("iphone_14");
            inventory.setQuantity(100);

            Inventory emptyInventory = new Inventory();
            emptyInventory.setSkuCode("iphone_14_pro");
            emptyInventory.setQuantity(0);

            inventoryRepository.save(inventory);
            inventoryRepository.save(emptyInventory);
        };
    }
}
