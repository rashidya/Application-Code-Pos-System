package lk.ijse.pos_system.entity;

public class Item {
    private String ItemCode;
    private String Item;
    private String Description;
    private String PackSize;
    private double UnitPrice;
    private double Discount;
    private int QtyOnHand;

    public Item() {
    }

    public Item(String itemCode, String item, String description, String packSize, double unitPrice, double discount, int qtyOnHand) {
        setItemCode(itemCode);
        setItem(item);
        setDescription(description);
        setPackSize(packSize);
        setUnitPrice(unitPrice);
        setDiscount(discount);
        setQtyOnHand(qtyOnHand);
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public int getQtyOnHand() {
        return QtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        QtyOnHand = qtyOnHand;
    }
}
