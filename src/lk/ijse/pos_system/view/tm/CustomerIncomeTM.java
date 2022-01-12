package lk.ijse.pos_system.view.tm;

public class CustomerIncomeTM {
    private String customerId;
    private String CustomerName;
    private double income;

    public CustomerIncomeTM() {
    }

    public CustomerIncomeTM(String customerId, String customerName, double income) {
        this.customerId = customerId;
        CustomerName = customerName;
        this.income = income;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public double getIncome() {
        return income;
    }
}
