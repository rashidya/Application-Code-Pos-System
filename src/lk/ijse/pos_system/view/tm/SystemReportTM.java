package lk.ijse.pos_system.view.tm;

public class SystemReportTM {
    private String itemCode;
    private int sales;
    private int waste;
    private double income;
    private double loss;
    private double profit;

    public SystemReportTM() {
    }

    public SystemReportTM(String itemCode, int sales, int waste, double income, double loss, double profit) {
        this.itemCode = itemCode;
        this.sales = sales;
        this.waste = waste;
        this.income = income;
        this.loss = loss;
        this.profit = profit;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getSales() {
        return sales;
    }

    public int getWaste() {
        return waste;
    }

    public double getIncome() {
        return income;
    }

    public double getLoss() {
        return loss;
    }

    public double getProfit() {
        return profit;
    }
}
