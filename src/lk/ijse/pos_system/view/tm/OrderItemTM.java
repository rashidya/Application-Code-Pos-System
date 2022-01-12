package lk.ijse.pos_system.view.tm;

public class OrderItemTM {
    private String itemCode;
    private String description;
    private int orderQty;
    private double unitPrice;

    public OrderItemTM() {
    }

    public OrderItemTM(String itemCode, String description, int orderQty, double unitPrice) {
        this.itemCode = itemCode;
        this.description = description;
        this.orderQty = orderQty;
        this.unitPrice = unitPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getDescription() {
        return description;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}
