package lk.ijse.pos_system.entity;

import java.time.LocalDate;

public class Order {
    private String orderId;
    private LocalDate orderDate;
    private String customerId;

    public Order() {
    }

    public Order(String orderId, LocalDate orderDate, String customerId) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setCustomerId(customerId);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
