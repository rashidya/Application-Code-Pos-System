 package lk.ijse.pos_system.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.custom.SystemReportsBO;
import lk.ijse.pos_system.dto.OrderDetailsDTO;
import lk.ijse.pos_system.repository.custom.Impl.CustomerDAOImpl;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.view.tm.CustomerIncomeTM;
import lk.ijse.pos_system.view.tm.SystemReportTM;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class SystemReportsFormController {
    public JFXDatePicker cmbTo;
    public JFXDatePicker cmbFrom;
    public Label lblTotalProfit;
    public Label lblTotalIncome;
    public Label lblTotalLoss;
    public JFXComboBox<String> cmbMonth;
    public JFXComboBox<Integer> cmbYear;
    public JFXDatePicker cmbDay;
    public AnchorPane systemReportsFormContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<SystemReportTM> tblReport;
    public TableColumn colItem;
    public TableColumn colSales;
    public TableColumn colWaste;
    public TableColumn colIncome;
    public TableColumn colLoss;
    public TableColumn colProfit;
    public JFXRadioButton radioBtnCustomer;
    public JFXRadioButton radioBtnItem;
    public TableView tblCustomer;
    public TableColumn colCustId;
    public TableColumn colCustomerName;
    public TableColumn colCustomerIncome;
    public Label lblIncome;
    public Label lblLoss;
    public Label lblProfit;
    public JFXButton btnDaily;
    public JFXButton btnMonthly;
    public JFXButton btnAnnually;

    ObservableList<String> months = FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12");
    ObservableList<Integer>  year=FXCollections.observableArrayList();

    ObservableList<SystemReportTM> report =FXCollections.observableArrayList();
    ObservableList<CustomerIncomeTM> customerReport =FXCollections.observableArrayList();

    SystemReportsBO systemReportsBO = (SystemReportsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SYSTEM_REPORTS);

    public void initialize(){
        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        // load Time
        {
            Thread clock = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            Platform.runLater(() -> {
                                lblTime.setText(new SimpleDateFormat("HH:mm a").format(new Date()));
                            });
                            sleep(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            clock.start();

        }
        //----------------set combo box------------------------
        cmbMonth.setItems(months);

        //-----------set columns-------
        colItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colSales.setCellValueFactory(new PropertyValueFactory<>("sales"));
        colWaste.setCellValueFactory(new PropertyValueFactory<>("waste"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
        colLoss.setCellValueFactory(new PropertyValueFactory<>("loss"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));


        colCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
        //--------------------------------------------------------

        cmbYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tblCustomer.getItems().clear();
            tblReport.getItems().clear();
            if(cmbMonth.isDisable()){
                try {
                    if (newValue!=null) {
                        viewAnnualReport(newValue);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    if (newValue!=null) {
                        viewMonthlyReport(newValue);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
            tblReport.setItems(report);
            setLabels();
        });

        radioBtnCustomer.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true) {


                radioBtnItem.setSelected(false);
                tblReport.setVisible(false);
                tblCustomer.setVisible(true);
                lblTotalProfit.setVisible(false);
                lblTotalIncome.setVisible(false);
                lblTotalLoss.setVisible(false);
                lblProfit.setVisible(false);
                lblLoss.setVisible(false);
                lblIncome.setVisible(false);
            }


        });

        radioBtnItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==true) {

                radioBtnCustomer.setSelected(false);
                tblReport.setVisible(true);
                tblCustomer.setVisible(false);
                lblTotalProfit.setVisible(true);
                lblTotalIncome.setVisible(true);
                lblTotalLoss.setVisible(true);
                lblProfit.setVisible(true);
                lblLoss.setVisible(true);
                lblIncome.setVisible(true);
            }
        });

    }

    //----------------------view reports-------------------------------------------
    public void viewDailyReportOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if(radioBtnCustomer.isSelected()){
            customerReport.clear();
            double income=0;
            for (String customer:systemReportsBO.getAllCustomerIds()
            ) {
                for (OrderDTO orderDTO :systemReportsBO.getOrdersFromDate(cmbDay.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                ) {
                    if(orderDTO.getCustomerId().equals(customer)){
                        for (OrderDetailsDTO orderItem:systemReportsBO.getItemsOfAnOrder(orderDTO.getOrderId())
                        ) {

                                income+=orderItem.getOrderQty()*systemReportsBO.searchItem(orderItem.getItemCode()).getUnitPrice();

                        }
                    }

                }
                CustomerIncomeTM reportItem =new CustomerIncomeTM(customer,new CustomerDAOImpl().search(customer).getCustName(),income);

                customerReport.add(reportItem);
            }
            tblCustomer.setItems(customerReport);
        }
        if(radioBtnItem.isSelected()){
            report.clear();
            int sales=0;
            for (ItemDTO itemDTO :systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO :systemReportsBO.getOrdersFromDate(cmbDay.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                ) {
                    for (OrderDetailsDTO orderItem:systemReportsBO.getItemsOfAnOrder(orderDTO.getOrderId())
                    ) {
                        if(orderItem.getItemCode().equals(itemDTO.getCode())){
                            sales+=orderItem.getOrderQty();
                        }
                    }
                }
                double discount = itemDTO.getDiscount()* itemDTO.getUnitPrice();
                SystemReportTM reportItem =new SystemReportTM(itemDTO.getCode(),sales,0,sales*(itemDTO.getUnitPrice()-discount),0,sales*(itemDTO.getUnitPrice()-discount));

                report.add(reportItem);
            }
            tblReport.setItems(report);
            setLabels();
        }

    }

    public void viewMonthlyReport(int newValue) throws SQLException, ClassNotFoundException  {
        if (radioBtnItem.isSelected()){
            int sales=0;
            for (ItemDTO itemDTO :systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO :systemReportsBO.getOrdersFromMonthOfYear(cmbMonth.getValue(),newValue)
                ) {
                    for (OrderDetailsDTO orderItem:systemReportsBO.getItemsOfAnOrder(orderDTO.getOrderId())
                    ) {
                        if(orderItem.getItemCode().equals(itemDTO.getCode())){
                            sales+=orderItem.getOrderQty();
                        }
                    }
                }
                double discount = itemDTO.getDiscount()* itemDTO.getUnitPrice();
                SystemReportTM reportItem =new SystemReportTM(itemDTO.getCode(),sales,0,sales*(itemDTO.getUnitPrice()-discount),0,sales*(itemDTO.getUnitPrice()-discount));

                report.add(reportItem);
            }
        }

        if (radioBtnItem.isSelected()){
            customerReport.clear();
            double income=0;
            for (String customer:systemReportsBO.getAllCustomerIds()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrdersFromMonthOfYear(cmbMonth.getValue(),newValue)
                ) {
                    if(orderDTO.getCustomerId().equals(customer)){
                        for (OrderDetailsDTO orderItem:systemReportsBO.getItemsOfAnOrder(orderDTO.getOrderId())
                        ) {

                            income+=orderItem.getOrderQty()*systemReportsBO.searchItem(orderItem.getItemCode()).getUnitPrice();

                        }
                    }

                }
                CustomerIncomeTM reportItem =new CustomerIncomeTM(customer,systemReportsBO.searchCustomer(customer).getName(),income);

                customerReport.add(reportItem);
            }
            tblCustomer.setItems(customerReport);
        }
    }

    public void viewAnnualReport(int newValue) throws SQLException, ClassNotFoundException {
        if (radioBtnItem.isSelected()){
            report.clear();
            int sales=0;

            for (ItemDTO itemDTO :systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrdersFromYear(newValue)
                ) {
                    for (OrderDetailsDTO orderItem : systemReportsBO.getItemsOfAnOrder(orderDTO.getOrderId())
                    ) {
                        if (orderItem.getItemCode().equals(itemDTO.getCode())) {
                            sales += orderItem.getOrderQty();
                        }
                    }
                }
                double discount = itemDTO.getDiscount() * itemDTO.getUnitPrice();
                SystemReportTM reportItem = new SystemReportTM(itemDTO.getCode(), sales, 0, sales * (itemDTO.getUnitPrice() - discount), 0, sales * (itemDTO.getUnitPrice() - discount));

                report.add(reportItem);
            }
            tblCustomer.setItems(customerReport);
        }

        if(radioBtnCustomer.isSelected()){
            customerReport.clear();
            double income=0;
            for (String customer:systemReportsBO.getAllCustomerIds()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrdersFromYear(newValue)
                ) {
                    if(orderDTO.getCustomerId().equals(customer)){
                        for (OrderDetailsDTO orderItem:systemReportsBO.getItemsOfAnOrder(orderDTO.getOrderId())
                        ) {

                            income+=orderItem.getOrderQty()*systemReportsBO.searchItem(orderItem.getItemCode()).getUnitPrice();

                        }
                    }

                }
                CustomerIncomeTM reportItem =new CustomerIncomeTM(customer,systemReportsBO.searchCustomer(customer).getName(),income);

                customerReport.add(reportItem);
            }
            tblCustomer.setItems(customerReport);
        }


    }

    public void viewSeasonalTrendsOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        report.clear();
        int sales=0;
        for (ItemDTO itemDTO :systemReportsBO.getAllItems()
        ) {
            for (OrderDTO orderDTO :systemReportsBO.getOrdersFromSeason(cmbFrom.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),cmbTo.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            ) {
                for (OrderDetailsDTO orderItem:systemReportsBO.getItemsOfAnOrder(orderDTO.getOrderId())
                ) {
                    if(orderItem.getItemCode().equals(itemDTO.getCode())){
                        sales+=orderItem.getOrderQty();
                    }
                }
            }
            double discount = itemDTO.getDiscount()* itemDTO.getUnitPrice();
            SystemReportTM reportItem =new SystemReportTM(itemDTO.getCode(),sales,0,sales*(itemDTO.getUnitPrice()-discount),0,sales*(itemDTO.getUnitPrice()-discount));

            report.add(reportItem);
        }
//        colSales.setSortType(TableColumn.SortType.DESCENDING);

        tblReport.setItems(report);
        colItem.setSortable(false);
        tblReport.getSortOrder().add(colSales);

        setLabels();
    }

    //------------------------------------------------------------------------------

    public void AnnuallyOnAction(ActionEvent actionEvent) {
        playMouseClickedAnimation(actionEvent);
        disableAll();
        cmbYear.setDisable(false);
        year.clear();
        for (int i = 2015; i <Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())); i++) {
            year.add(i);
        }
        cmbYear.setItems(year);
        tblCustomer.getItems().clear();
        tblReport.getItems().clear();
    }

    public void MonthlyOnAction(ActionEvent actionEvent) {
        playMouseClickedAnimation(actionEvent);
        disableAll();
        cmbMonth.setDisable(false);
        cmbYear.setDisable(false);
        year.clear();
        for (int i = 2015; i <=Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())); i++) {
            year.add(i);
        }
        cmbYear.setItems(year);
        tblCustomer.getItems().clear();
        tblReport.getItems().clear();
    }

    public void DailyOnAction(ActionEvent actionEvent) {
        playMouseClickedAnimation(actionEvent);
        disableAll();
        cmbDay.setDisable(false);
        tblCustomer.getItems().clear();
        tblReport.getItems().clear();

    }
    
    private void setLabels() {
        double income = 0;
        double loss = 0;
        for (SystemReportTM temp : report
        ) {
            income += temp.getIncome();
            loss += temp.getLoss();
        }

        lblTotalLoss.setText(String.valueOf(loss));
        lblTotalIncome.setText(String.valueOf(income));
        lblTotalProfit.setText(String.valueOf(income-loss));
    }

    private void disableAll(){
        cmbDay.setDisable(true);
        cmbMonth.setDisable(true);
        cmbYear.setDisable(true);
    }


    public void openAdminFormOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) systemReportsFormContext.getScene().getWindow();
        window.setY(250);
        window.setX(600);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminForm.fxml"))));
    }


    private void playMouseClickedAnimation(ActionEvent event){
        cmbMonth.getSelectionModel().clearSelection();
        ObservableList<JFXButton> btn=FXCollections.observableArrayList(btnAnnually,btnDaily,btnMonthly);

        if (event.getSource() instanceof JFXButton) {
            JFXButton icon = (JFXButton) event.getSource();

            for (JFXButton jfxButton : btn) {
                ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), jfxButton);
                if (!jfxButton.getId().equals(icon.getId())){
                    scaleT.setToX(1);
                    scaleT.setToY(1);
                }else {
                    scaleT.setToX(1.1);
                    scaleT.setToY(1.1);
                }
                scaleT.play();
            }


        }
    }



}
