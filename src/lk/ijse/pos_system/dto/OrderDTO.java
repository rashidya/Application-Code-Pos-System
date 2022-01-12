package lk.ijse.pos_system.dto;

import java.util.ArrayList;

public class OrderDTO {
    private String orderId;
    private String orderDate;
    private String customerId;
    private ArrayList<OrderDetailsDTO> orderItems;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String orderDate, String customerId, ArrayList<OrderDetailsDTO> orderItems) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.orderItems = orderItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public ArrayList<OrderDetailsDTO> getOrderItems() {
        return orderItems;
    }
}
