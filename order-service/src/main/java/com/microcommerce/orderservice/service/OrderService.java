package com.microcommerce.orderservice.service;

import com.microcommerce.orderservice.dto.OrderItemDto;
import com.microcommerce.orderservice.model.Order;
import com.microcommerce.orderservice.model.OrderItem;
import com.microcommerce.orderservice.request.OrderRequest;
import com.microcommerce.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(this::OrderItemsTransformer).toList();

        order.setOrderItemList(orderItems);

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
