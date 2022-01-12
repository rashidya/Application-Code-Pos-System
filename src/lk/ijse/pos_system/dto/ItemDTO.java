package lk.ijse.pos_system.dto;

public class ItemDTO {
    private String code;
    private String item;
    private String description;
    private String packSize;
    private Double unitPrice;
    private Double discount;
    private int qtyOnHand;

    public ItemDTO() {
    }

    public ItemDTO(String code, String item, String description, String packSize, Double unitPrice, Double discount, int qtyOnHand) {
        this.code = code;
        this.item = item;
        this.description = description;
        this.packSize = packSize;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.qtyOnHand = qtyOnHand;
    }

    public String getCode() {
        return code;
    }

    public String getItem() {
        return item;
    }

    public String getDescription() {
        return description;
    }

    public String getPackSize() {
        return packSize;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }
}
