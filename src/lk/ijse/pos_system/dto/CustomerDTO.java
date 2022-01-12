package lk.ijse.pos_system.dto;

public class CustomerDTO {
    private String id;
    private String name;
    private String title;
    private String address;
    private String city;
    private String province;
    private String postalCode;

    public CustomerDTO() {
    }

    public CustomerDTO(String id, String name, String title, String address, String city, String province, String postalCode) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
