package lk.ijse.pos_system.entity;

public class OrderDetail {
    private String OrderId;
    private String ItemCode;
    private int OrderQty;
    private double Discount;

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String itemCode, int orderQty, double discount) {
        setOrderId(orderId);
        setItemCode(itemCode);
        setOrderQty(orderQty);
        setDiscount(discount);
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public int getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(int orderQty) {
        OrderQty = orderQty;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }
}
