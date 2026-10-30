
package com.ecommerce.service;

import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.OrderResponse;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {

        Order order = new Order();

        order.setCustomerName(request.getCustomerName());
        order.setEmail(request.getEmail());
        order.setShippingAddress(request.getShippingAddress());
        order.setPincode(request.getPincode());

        BigDecimal totalAmount = BigDecimal.ZERO;
        Set<Long> productIds = new HashSet<>();

        for (OrderRequest.OrderItemRequest itemRequest
                : request.getItems()) {

            Long productId = itemRequest.getProductId();
            Integer quantity = itemRequest.getQuantity();

            // Prevent duplicate product entries in one order.
            if (!productIds.add(productId)) {
                throw new IllegalArgumentException(
                        "Duplicate product ID in order: " + productId);
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ProductNotFoundException(
                                    "Product not found with id: " + productId));

            // Validate stock availability.
            if (product.getStock() < quantity) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + product.getName());
            }

            // Always use the price stored in the database.
            BigDecimal unitPrice = product.getPrice();

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(quantity));

            totalAmount = totalAmount.add(subtotal);

            // Create the order item.
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setQuantity(quantity);

            order.addItem(orderItem);

            // Reduce available stock.
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);

        // Save the order and its items together.
        Order savedOrder = orderRepository.save(order);

        return convertToResponse(savedOrder);
    }


    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    private OrderResponse convertToResponse(Order order) {

        List<OrderResponse.OrderItemResponse> itemResponses =
                new ArrayList<>();

        for (OrderItem item : order.getItems()) {

            BigDecimal subtotal = item.getUnitPrice().multiply(
                    BigDecimal.valueOf(item.getQuantity()));

            itemResponses.add(
                    new OrderResponse.OrderItemResponse(
                            item.getProduct().getId(),
                            item.getProductName(),
                            item.getUnitPrice(),
                            item.getQuantity(),
                            subtotal
                    )
            );
        }

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate(),
                itemResponses
        );
    }
}
