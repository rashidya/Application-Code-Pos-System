package lk.ijse.pos_system.dto;

public class OrderDetailsDTO {
    private String orderId;
    private String itemCode;
    private int orderQty;
    private double discount;

    public OrderDetailsDTO() {
    }

    public OrderDetailsDTO(String orderId, String itemCode, int orderQty, double discount) {
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.orderQty = orderQty;
        this.discount = discount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public double getDiscount() {
        return discount;
    }
}
