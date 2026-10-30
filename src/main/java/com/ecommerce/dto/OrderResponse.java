
package com.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private String customerName;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderItemResponse> items;

    public OrderResponse() {
    }

    public OrderResponse(
            Long orderId,
            String customerName,
            BigDecimal totalAmount,
            String status,
            LocalDateTime orderDate,
            List<OrderItemResponse> items) {

        this.orderId = orderId;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public static class OrderItemResponse {

        private Long productId;
        private String productName;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;

        public OrderItemResponse() {
        }

        public OrderItemResponse(
                Long productId,
                String productName,
                BigDecimal unitPrice,
                Integer quantity,
                BigDecimal subtotal) {

            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }
    }
}
