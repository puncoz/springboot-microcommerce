package com.microcommerce.orderservice.service;

import com.microcommerce.inventoryservice.response.InventoryResponse;
import com.microcommerce.orderservice.dto.OrderItemDto;
import com.microcommerce.orderservice.model.Order;
import com.microcommerce.orderservice.model.OrderItem;
import com.microcommerce.orderservice.repository.OrderRepository;
import com.microcommerce.orderservice.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;


    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(this::OrderItemsTransformer).toList();

        order.setOrderItemList(orderItems);

        List<String> skuCodes = order.getOrderItemList().stream().map(OrderItem::getSkuCode).toList();

        // call inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponses = webClientBuilder.build()
                .get()
                .uri("http://inventory-service/api/inventories/check", uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes)
                        .build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (Boolean.FALSE.equals(allProductsInStock)) {
            throw new IllegalArgumentException("Some product are not in stock, please try again later.");
        }

        orderRepository.save(order);
    }

    private OrderItem OrderItemsTransformer(OrderItemDto item) {
        OrderItem orderItem = new OrderItem();

        orderItem.setSkuCode(item.getSkuCode());
        orderItem.setPrice(item.getPrice());
        orderItem.setQuantity(item.getQuantity());

        return orderItem;
    }
}
