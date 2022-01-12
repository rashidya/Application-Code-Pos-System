package lk.ijse.pos_system.entity;

public class Customer {
    private String CustId;
    private String CustName;
    private String CustTitle;
    private String CustAddress;
    private String City;
    private String Province;
    private String PostalCode;

    public Customer() {
    }

    public Customer(String custId, String custName, String custTitle, String custAddress, String city, String province, String postalCode) {
        CustId = custId;
        CustName = custName;
        CustTitle = custTitle;
        CustAddress = custAddress;
        City = city;
        Province = province;
        PostalCode = postalCode;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public void setCustTitle(String custTitle) {
        CustTitle = custTitle;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getCustId() {
        return CustId;
    }

    public String getCustName() {
        return CustName;
    }

    public String getCustTitle() {
        return CustTitle;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public String getCity() {
        return City;
    }

    public String getProvince() {
        return Province;
    }

    public String getPostalCode() {
        return PostalCode;
    }
}
