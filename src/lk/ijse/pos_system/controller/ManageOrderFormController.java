package lk.ijse.pos_system.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.custom.ManageOrderBO;
import lk.ijse.pos_system.report.CustomerOrderReport;
import lk.ijse.pos_system.repository.custom.Impl.ItemDAOImpl;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDetailsDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.validation.Validation;
import lk.ijse.pos_system.view.tm.CartTM;
import lk.ijse.pos_system.view.tm.OrderItemTM;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class ManageOrderFormController {
    public AnchorPane manageOrderFormContext;
    public Label lblTime;
    public Label lblDate;
    public TextField txtQtyOnHand;
    public JFXButton btnAddOrUpdate;
    public TextField txtDiscount;
    public ComboBox<String> cmbItem;
    public TextField txtQty;
    public JFXButton btnRemove;
    public ComboBox<String> cmbCustomerId;
    public TableView<OrderItemTM> tblOrderItems;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colOrderQty;
    public TableColumn colUnitPrice;
    public ComboBox<String> cmbOrder;
    public TextField txtPackSize;
    public TextField txtUnitPrice;
    public TextField txtItem;
    public TextField txtDescription;
    public Label lblGrossTotal;
    public Label lblDiscount;
    public Label lblNetTotal;
    public JFXButton btnEditOrder;
    public TextField txtCustomerIdBill;
    public TextField txtOrderIdBill;
    public Text lblDateAndTime;
    public TableView tblBill;
    public TableColumn colItemCodeBill;
    public TableColumn colItemBill;
    public TableColumn colQtyBill;
    public TableColumn colUnitPriceBill;
    public TableColumn colAmountBill;
    public Label lblGrossTotalBill;
    public Label lblDiscountBill;
    public Label lblNetTotalBill;
    public Label lblChangeBill;
    public TextField txtCashBill;
    ObservableList<OrderItemTM> orderItemList = FXCollections.observableArrayList();

    ManageOrderBO manageOrderBO = (ManageOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    LinkedHashMap<TextField, Pattern> mapQty = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> mapCash = new LinkedHashMap<>();

    public void initialize(){

        storeValidations();

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


        try {
            cmbCustomerId.setItems(manageOrderBO.getAllCustomerIds());
            cmbItem.setItems(manageOrderBO.getAllItemCodes());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //tblItem
        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("orderQty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        //tblBill
        colItemCodeBill.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemBill.setCellValueFactory(new PropertyValueFactory<>("item"));
        colQtyBill.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPriceBill.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colAmountBill.setCellValueFactory(new PropertyValueFactory<>("total"));

        //----------------------listeners------------------------

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue!=null) {
                    cmbOrder.setItems(manageOrderBO.getAllOrderIdsOfACustomer(newValue));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

        cmbOrder.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {

                if(newValue!=null) {
                    ObservableList<OrderDetailsDTO> itemsOfAnOrder = manageOrderBO.getItemsOfAnOrder(newValue);
                    for (OrderDetailsDTO temp : itemsOfAnOrder) {

                        ItemDTO item= manageOrderBO.searchItem(temp.getItemCode());
                        orderItemList.add(new OrderItemTM(item.getCode(),item.getDescription(),temp.getOrderQty(),item.getUnitPrice()));
                    }
                    tblOrderItems.setItems(orderItemList);
                    calculateOrderTotal();
                    makeBill();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        cmbItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue!=null) {
                    setItemDetails(newValue);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblOrderItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                btnAddOrUpdate.setText("Update");
                btnRemove.setDisable(false);
                cmbItem.setValue(newValue.getItemCode());
                txtQty.setText(String.valueOf(newValue.getOrderQty()));

            }

        });

        txtQty.textProperty().addListener(observable -> {
            Validation.validate(mapQty);
        });

        txtCashBill.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Validation.validate(mapCash)){
                boolean cashGreaterThanTotal = Double.parseDouble(lblNetTotalBill.getText())<=Double.parseDouble(txtCashBill.getText());
                if (cashGreaterThanTotal){
                    txtCashBill.setStyle("-fx-border-color: green;"+"-fx-border-width: 2");
                }else {
                    txtCashBill.setStyle("-fx-border-color: red;"+"-fx-border-width: 2");
                }
            }
        });

    }

    public void storeValidations(){
        mapQty.put(txtQty, Validation.number);
        mapCash.put(txtCashBill,Validation.decimalNumber);
    }


    public void AddOrUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (cmbOrder.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please Select an Order").show();
            return;
        }

        if(Validation.validate(mapQty)){
            if(checkQty()!=0) {
                OrderItemTM orderItemTM = new OrderItemTM(cmbItem.getValue(), txtDescription.getText(),checkQty(), Double.parseDouble(txtUnitPrice.getText()));
                if (btnAddOrUpdate.getText().equals("Add Item")) {
                    if (isExistsItem(cmbItem.getValue())) {
                        for (int i = 0; i < orderItemList.size(); i++) {
                            if (orderItemList.get(i).getItemCode().equals(cmbItem.getValue())) {
                                orderItemList.set(i, orderItemTM);
                            }
                        }
                    }else{
                        orderItemList.add(orderItemTM);
                    }
                } else {
                    for (int i = 0; i < orderItemList.size(); i++) {
                        if (orderItemList.get(i).getItemCode().equals(cmbItem.getValue())) {
                            orderItemList.set(i, orderItemTM);
                        }
                    }
                }

                tblOrderItems.setItems(orderItemList);
                clearAllItemDetails();
                btnRemove.setDisable(true);
                btnAddOrUpdate.setText("Add Item");
                calculateOrderTotal();
            }
        }
    }

    public void removeItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to remove this item ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.orElse(no)==yes){
            for (int i=0;i<orderItemList.size();i++){
                if(orderItemList.get(i).getItemCode().equals(cmbItem.getValue())){
                    orderItemList.remove(i);
                }
            }

        }

        tblOrderItems.setItems(orderItemList);
        calculateOrderTotal();
        if (orderItemList.size()==0){
            btnEditOrder.setDisable(true);
            clearAllItemDetails();
            btnRemove.setDisable(true);
            btnAddOrUpdate.setText("Add Item");
        }


    }

    public void editOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (cmbOrder.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please Select an Order").show();
            return;
        }


        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to Edit this Order ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();


        if(result.orElse(no)==yes) {
            ArrayList<OrderDetailsDTO> orderItems =new ArrayList<>();
            for (OrderItemTM temp:orderItemList
                 ) {
                OrderDetailsDTO orderItem =new OrderDetailsDTO(cmbOrder.getValue(),temp.getItemCode(),temp.getOrderQty(),new ItemDAOImpl().search(temp.getItemCode()).getDiscount());
                orderItems.add(orderItem);
            }
            OrderDTO orderDTO = new OrderDTO(cmbOrder.getValue(),lblDate.getText(),cmbCustomerId.getValue(),orderItems);

            if(manageOrderBO.updateOrder(orderDTO)){
                new Alert(Alert.AlertType.INFORMATION,"You Order has been Updated Successfully").show();
                makeBill();
                clearAll();
            }else {
                new Alert(Alert.AlertType.WARNING,"Something went wrong.Try Again").show();
            }
        }
    }

    public void cancelOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (cmbOrder.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please Select an Order").show();
            return;
        }

        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to Cancel this Order ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.orElse(no)==yes) {
            manageOrderBO.deleteOrder(cmbOrder.getValue());
        }
        clearAll();
    }

    private void setItemDetails(String itemCode) throws SQLException, ClassNotFoundException {
        ItemDTO itemDTO =manageOrderBO.searchItem(itemCode);
        txtDiscount.setText(String.valueOf(itemDTO.getDiscount()));
        txtQtyOnHand.setText(String.valueOf(itemDTO.getQtyOnHand()));
        txtUnitPrice.setText(String.valueOf(itemDTO.getUnitPrice()));
        txtItem.setText(String.valueOf(itemDTO.getItem()));
        txtPackSize.setText(String.valueOf(itemDTO.getPackSize()));
        txtDescription.setText(String.valueOf(itemDTO.getDescription()));
    }

    private void calculateOrderTotal() throws SQLException, ClassNotFoundException {
        double grossTotal=0;
        double discount=0;


        for (OrderItemTM temp:orderItemList
        ) {
            grossTotal+=temp.getOrderQty()*temp.getUnitPrice();
            discount+=temp.getOrderQty()*(manageOrderBO.searchItem(temp.getItemCode()).getDiscount());
        }

        DecimalFormat df = new DecimalFormat("#.00");
        lblGrossTotal.setText(df.format(grossTotal));
        lblDiscount.setText(df.format(discount));
        lblNetTotal.setText(df.format(grossTotal-discount));

    }

    private int checkQty(){
        int qty=0;
        if(!txtQty.getText().isEmpty()) {
            qty=Integer.parseInt(txtQty.getText());
        }

        if(isExistsItem(cmbItem.getValue())){

            for (int i=0;i<orderItemList.size();i++) {
                if(orderItemList.get(i).getItemCode().equals(cmbItem.getValue())){
                    qty=qty+orderItemList.get(i).getOrderQty();
                }
            }

        }
        if(txtQty.getText().isEmpty() || Integer.parseInt(txtQtyOnHand.getText()) < qty) {
            new Alert(Alert.AlertType.WARNING, "Invalid Quantity").show();
            return 0;
        }

        return qty;


    }

    private boolean isExistsItem(String code){
        for (int i=0;i<orderItemList.size();i++) {
            if(orderItemList.get(i).getItemCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    private void clearAll() throws SQLException, ClassNotFoundException {
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbOrder.getSelectionModel().clearSelection();
        clearAllItemDetails();
        orderItemList.clear();
        tblOrderItems.setItems(orderItemList);
        calculateOrderTotal();
        btnAddOrUpdate.setText("Add Item");
        btnRemove.setDisable(true);
        btnEditOrder.setDisable(false);
    }

    private void clearAllItemDetails(){
        cmbItem.getSelectionModel().clearSelection();
        txtItem.clear();
        txtDescription.clear();
        txtDiscount.clear();
        txtUnitPrice.clear();
        txtPackSize.clear();
        txtQty.clear();
        txtQtyOnHand.clear();
        txtQty.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");

    }

    public void openUserFormOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) manageOrderFormContext.getScene().getWindow();
        window.setY(250);
        window.setX(600);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/userForm.fxml"))));
    }

    private void makeBill() throws SQLException, ClassNotFoundException {
        ObservableList<CartTM> orderItemsBill=FXCollections.observableArrayList();
        for (OrderItemTM temp:orderItemList
             ) {
            double total =temp.getOrderQty()*temp.getUnitPrice();
            ItemDTO itemDTO =manageOrderBO.searchItem(temp.getItemCode());
            CartTM billItem =new CartTM(temp.getItemCode(), itemDTO.getItem(),temp.getOrderQty(),temp.getUnitPrice(), itemDTO.getDiscount(),total);
            orderItemsBill.add(billItem);
        }
        txtOrderIdBill.setText(cmbOrder.getValue());
        txtCustomerIdBill.setText(cmbCustomerId.getValue());
        lblDateAndTime.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
        lblGrossTotalBill.setText(lblGrossTotal.getText());
        lblNetTotalBill.setText(lblNetTotal.getText());
        lblDiscountBill.setText(lblDiscount.getText());


        tblBill.setItems(orderItemsBill);


    }

    public void printBillOnAction(ActionEvent actionEvent) {


        HashMap map =new HashMap();
        map.put("GrossTotal",lblGrossTotalBill.getText());
        map.put("TotalDiscount",lblDiscountBill.getText());
        map.put("NetTotal",lblDiscountBill.getText());
        map.put("Cash",Double.parseDouble(txtCashBill.getText()));
        map.put("Change",lblChangeBill.getText());
        map.put("OrderId",txtOrderIdBill.getText());
        map.put("CustomerId",txtCustomerIdBill.getText());

        ObservableList<CartTM> items = tblBill.getItems();
        CustomerOrderReport.generateInvoice(map,items);
        clearBillDetails();
    }

    private void clearBillDetails(){
        lblDateAndTime.setText("");
        txtOrderIdBill.clear();
        txtCustomerIdBill.clear();
        txtCashBill.clear();
        lblNetTotalBill.setText("0.00");
        lblDiscountBill.setText("0.00");
        lblGrossTotalBill.setText("0.00");
        lblChangeBill.setText("0.00");
        tblBill.setItems(FXCollections.observableArrayList());
        txtCashBill.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");
    }

    public void calculateChangeOnAction(ActionEvent actionEvent) {

        if (Validation.validate(mapCash)){
            boolean cashGreaterThanTotal = Double.parseDouble(lblNetTotalBill.getText())<=Double.parseDouble(txtCashBill.getText());
            if (cashGreaterThanTotal) {
                double change = Double.parseDouble(txtCashBill.getText()) - Double.parseDouble(lblNetTotalBill.getText());
                lblChangeBill.setText(String.valueOf(new DecimalFormat("#.00").format(change)));
            }else {
                txtCashBill.setStyle("-fx-border-color: red;"+"-fx-border-width: 2");
            }
        }
    }

    public void checkCustomeridOnAction(MouseEvent mouseEvent) {
        if (cmbCustomerId.getValue().isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Please Select an Order").show();
        }
    }


}
