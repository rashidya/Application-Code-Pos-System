package lk.ijse.pos_system.view.tm;

public class CartTM {
    private String itemCode;
    private String item;
    private int qty;
    private double unitPrice;
    private double discount;
    private double total;

    public CartTM() {
    }

    public CartTM(String itemCode, String item, int qty, double unitPrice, double discount, double total) {
        this.itemCode = itemCode;
        this.item = item;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.total = total;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItem() {
        return item;
    }

    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotal() {
        return total;
    }
}
