package lk.ijse.pos_system.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.custom.PurchaseOrderBO;
import lk.ijse.pos_system.dto.*;
import lk.ijse.pos_system.report.CustomerOrderReport;
import lk.ijse.pos_system.validation.Validation;
import lk.ijse.pos_system.view.tm.CartTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class PlaceOrderFormController {
    public AnchorPane placeOrderFormContext;
    public Label lblTime;
    public Label lblDate;
    public JFXButton btnSave;
    public JFXButton btnPlaceOrder;
    public TextField txtId;
    public JFXTextField txtName;
    public JFXComboBox<String> cmbTitle;
    public JFXTextField txtAddress;
    public JFXComboBox<String> cmbProvince;
    public JFXTextField txtCity;
    public JFXTextField txtPostalCode;
    public ComboBox<String> cmbCustIds;
    public TextField txtItem;
    public TextField txtPackSize;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtDescription;
    public TextField txtDiscount;
    public ComboBox<String> cmbItem;
    public TextField txtQty;
    public JFXButton btnAddToCart;
    public TableView<CartTM> tblCart;
    public TableColumn colItemCode;
    public TableColumn colItem;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public JFXButton btnRemove;
    public Label lblGrossTotal;
    public Label lblDiscount;
    public Label lblNetTotal;
    public TextField txtOrderId;
    public TextField txtCustomerIdBill;
    public TextField txtOrderIdBill;
    public TableView<CartTM> tblBill;
    public TableColumn colItemCodeBill;
    public TableColumn colItemBill;
    public TableColumn colQtyBill;
    public TableColumn colUnitPriceBill;
    public TableColumn colAmountBill;
    public Text txtDateAndTimeBill;
    public Label lblGrossTotalBill;
    public Label lblDiscountBill;
    public Label lblNetTotalBill;
    public Label lblChangeBill;
    public TextField txtCashBill;



    private ObservableList<CartTM> cartTMObservableList=FXCollections.observableArrayList();
    PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PURCHASE_ORDER);

    LinkedHashMap<TextField, Pattern> mapCustomer =new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> mapQty =new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> mapCash=new LinkedHashMap<>();

    private OrderDTO currentOrder;

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

        ObservableList<String> titles =FXCollections.observableArrayList("Mr","Mrs","Miss");
        ObservableList<String> provinces =FXCollections.observableArrayList("Southern","Western","Eastern","Northern","Central","North Central","North Western","Uva","Sabaragamuwa");

        try {
            cmbTitle.setItems(titles);
            cmbProvince.setItems(provinces);
            cmbCustIds.setItems(purchaseOrderBO.getAllCustomerIds());
            cmbItem.setItems(purchaseOrderBO.getAllItemCodes());
            txtOrderId.setText(purchaseOrderBO.generateNewOrderId());

            txtId.setText(purchaseOrderBO.generateNewCustomerId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //-tblCart
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        //-tblBill
        colItemCodeBill.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemBill.setCellValueFactory(new PropertyValueFactory<>("item"));
        colQtyBill.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPriceBill.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colAmountBill.setCellValueFactory(new PropertyValueFactory<>("total"));


        //------------listeners------------------------------

        cmbCustIds.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue!=null&&!newValue.equals("Customer Id")) {
                    setCustomerDetails(newValue);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


        cmbItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue!=null&&!newValue.equals("Item Code")) {
                    setItemsDetails(newValue);
                    btnAddToCart.setDisable(false);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

                if(newValue!=null) {
                    txtQty.setText(String.valueOf(newValue.getQty()));
                    cmbItem.setValue(newValue.getItemCode());
                    btnRemove.setDisable(false);
                    btnAddToCart.setText("Update");
                    btnAddToCart.setDisable(false);
                }


        });

        for (TextField textField : mapCustomer.keySet()) {
            textField.textProperty().addListener(observable -> {
                Validation.validate(mapCustomer);
            });
        }

        txtQty.textProperty().addListener(observable -> {
            Validation.validate(mapQty);
        });
        txtCashBill.textProperty().addListener(observable -> {
            if (Validation.validate(mapCash)){
                boolean cashGreaterThanTotal = Double.parseDouble(lblNetTotalBill.getText())<=Double.parseDouble(txtCashBill.getText());
                if (cashGreaterThanTotal) {
                    txtCashBill.setStyle("-fx-border-color: green;"+"-fx-border-width: 2");
                }else {
                    txtCashBill.setStyle("-fx-border-color: red;"+"-fx-border-width: 2");
                }
            }
        });
    }

    private void storeValidations(){
        mapCustomer.put(txtName, Validation.personName);
        mapCustomer.put(txtAddress, Validation.text);
        mapCustomer.put(txtCity, Validation.city);
        mapCustomer.put(txtPostalCode, Validation.postal);

        mapQty.put(txtQty, Validation.number);
        mapCash.put(txtCashBill, Validation.decimalNumber);
    }

//----------------------------Customer-----------------------------------------------------------------
    private void setCustomerDetails(String id) throws SQLException, ClassNotFoundException {
        CustomerDTO customerDTO = purchaseOrderBO.searchCustomer(id);
        txtId.setText(customerDTO.getId());
        txtName.setText(customerDTO.getName());
        cmbTitle.setValue(customerDTO.getTitle());
        txtAddress.setText(customerDTO.getAddress());
        txtCity.setText(customerDTO.getCity());
        cmbProvince.setValue(customerDTO.getProvince());
        txtPostalCode.setText(customerDTO.getPostalCode());
    }

    public void saveNewCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(validateCustomer()){
            CustomerDTO customerDTO =new CustomerDTO(txtId.getText(),txtName.getText(),cmbTitle.getValue(),txtAddress.getText(),txtCity.getText(),cmbProvince.getValue(),txtPostalCode.getText());
            if (purchaseOrderBO.addCustomer(customerDTO)){
                new Alert(Alert.AlertType.INFORMATION,"Customer has been Added Successfully").show();
            }else{
                new Alert(Alert.AlertType.WARNING,"Something Wrong.Try Again").show();
            }

            cmbCustIds.setItems(purchaseOrderBO.getAllCustomerIds());
        }

    }

    private boolean validateCustomer() {
        if (Validation.validate(mapCustomer)){
            if (!cmbTitle.getValue().isEmpty()){
                if (!cmbProvince.getValue().isEmpty()){
                    return true;
                }else {
                    new Alert(Alert.AlertType.WARNING,"Please select a Province").show();
                    return false;
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Please select a title").show();
                return false;
            }
        }else {

            return false;
        }
    }

    public void allowAddNewCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearAllCustomerFields();
        cmbCustIds.setDisable(true);
        btnSave.setDisable(false);
        editableAllTextFields();

    }

    private void editableAllTextFields(){
        for (TextField textField : mapCustomer.keySet()) {
            textField.setEditable(true);
        }



    }

    private void clearAllCustomerFields() throws SQLException, ClassNotFoundException {
        cmbCustIds.getSelectionModel().clearSelection();
        cmbTitle.getSelectionModel().clearSelection();
        cmbProvince.getSelectionModel().clearSelection();
        txtId.setText(purchaseOrderBO.generateNewCustomerId());

        for (TextField textField : mapCustomer.keySet()) {
            textField.clear();
            textField.setEditable(false);
            textField.setStyle("-fx-border-color: white; -fx-border-width: 1;" );
        }
    }


//---------------------------------------------------------------------------------------------------------------------


//---------------------------------------ITEM--------------------------------------------------------------------


    private void setItemsDetails(String code) throws SQLException, ClassNotFoundException {
        ItemDTO itemDTO = purchaseOrderBO.searchItem(code);
        txtItem.setText(itemDTO.getItem());
        txtDescription.setText(itemDTO.getDescription());
        txtPackSize.setText(itemDTO.getPackSize());
        txtUnitPrice.setText(String.valueOf(itemDTO.getUnitPrice()));
        txtDiscount.setText(String.valueOf(itemDTO.getDiscount()*100));
        txtQtyOnHand.setText(String.valueOf(itemDTO.getQtyOnHand()));


    }

    public void AddToCartOnAction(ActionEvent actionEvent) {
        if(Validation.validate(mapQty)){
            int qty=checkAndGetQty();
            //check qty--------------
            if (qty!=0){
                double discount = Double.parseDouble(txtUnitPrice.getText()) * Double.parseDouble(txtDiscount.getText()) / 100 * qty;
                double total = Double.parseDouble(txtUnitPrice.getText()) * qty;

                CartTM cartTM = new CartTM(cmbItem.getValue(), txtItem.getText(),qty, Double.parseDouble(txtUnitPrice.getText()), discount, total);

                if(btnAddToCart.getText().equals("Add to Cart")) {

                    if (isExistsItem(cartTM.getItemCode())){
                        for (int i=0;i<cartTMObservableList.size();i++) {
                            if(cartTMObservableList.get(i).getItemCode().equals(cartTM.getItemCode())){
                                cartTMObservableList.set(i,cartTM);
                            }
                        }
                    }else {
                        cartTMObservableList.add(cartTM);
                        if(btnPlaceOrder.isDisable())btnPlaceOrder.setDisable(false);
                    }

                }

                if(btnAddToCart.getText().equals("Update")){

                    for (int i=0;i<cartTMObservableList.size();i++) {
                        if(cartTMObservableList.get(i).getItemCode().equals(cartTM.getItemCode())){
                            cartTMObservableList.set(i,cartTM);
                        }
                    }
                    btnAddToCart.setText("Add to Cart");
                }
                clearAllItemFields();
                loadTotal();
                btnAddToCart.setDisable(true);
                btnRemove.setDisable(true);
                tblCart.setItems(cartTMObservableList);
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

            for (int i=0;i<cartTMObservableList.size();i++) {
                if(cartTMObservableList.get(i).getItemCode().equals(cmbItem.getValue())){
                    cartTMObservableList.remove(i);
//                    clearAllItemFields();
                }
            }

        }
        tblCart.setItems(cartTMObservableList);
        if(cartTMObservableList.isEmpty()){
            btnPlaceOrder.setDisable(true);
            clearAllItemFields();
        }
        loadTotal();
    }

    private boolean isExistsItem(String code){
        for (int i=0;i<cartTMObservableList.size();i++) {
            if(cartTMObservableList.get(i).getItemCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    private int checkAndGetQty(){
        int qty=0;
        if(!txtQty.getText().isEmpty()) {
            qty=Integer.parseInt(txtQty.getText());
        }

        if(isExistsItem(cmbItem.getValue())){

            for (int i=0;i<cartTMObservableList.size();i++) {
                if(cartTMObservableList.get(i).getItemCode().equals(cmbItem.getValue())){
                    qty+=cartTMObservableList.get(i).getQty();
                }
            }

        }
        if(txtQty.getText().isEmpty() || Integer.parseInt(txtQtyOnHand.getText()) < qty) {
                new Alert(Alert.AlertType.WARNING, "Invalid Quantity").show();
                return 0;
        }

        return qty;


    }

    private void clearAllItemFields(){
        cmbItem.setValue("Item Code");
        txtItem.clear();
        txtDescription.clear();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtDiscount.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
        txtQty.setStyle("-fx-border-color: silver; -fx-border-width: 1;" );

    }

    private void loadTotal(){
        DecimalFormat df = new DecimalFormat("#.00");
        double grossTotal=0;
        double discount=0;
        for (CartTM temp:cartTMObservableList
             ) {
            grossTotal+=temp.getTotal();
            discount+= temp.getDiscount();
        }

        lblGrossTotal.setText(df.format(grossTotal));
        lblDiscount.setText(df.format(discount));
        lblNetTotal.setText(df.format(grossTotal-discount));
    }

    //------------------------------------------------------------------------------


    //---------------------------Order-----------------------------------------

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(txtId.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Please Select a Customer.").show();
            return;
        }

        ArrayList<OrderDetailsDTO> orderItems=new ArrayList<>();

        for (CartTM temp:cartTMObservableList
             ) {
            OrderDetailsDTO orderItem = new OrderDetailsDTO(txtOrderId.getText(),temp.getItemCode(),temp.getQty(),temp.getDiscount());
            orderItems.add(orderItem);
        }

        OrderDTO orderDTO =new OrderDTO(txtOrderId.getText(),lblDate.getText(),txtId.getText(),orderItems);
        currentOrder=orderDTO;

        if(purchaseOrderBO.placeOrder(orderDTO)){
            new Alert(Alert.AlertType.INFORMATION,"Your Order has been Placed Successfully").show();
            makeBill();
            clearAll();
            txtOrderId.setText(purchaseOrderBO.generateNewOrderId());
        }else {
            new Alert(Alert.AlertType.WARNING,"Something Wrong.Try Again").show();
        }
    }

    public void cancelOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to cancel this order ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.orElse(no)==yes) {
            clearAll();
        }
    }

    //--------------------------------------------------------------------

    private void makeBill(){
        txtOrderIdBill.setText(txtOrderId.getText());
        txtCustomerIdBill.setText(txtId.getText());
        txtDateAndTimeBill.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
        lblGrossTotalBill.setText(lblGrossTotal.getText());
        lblNetTotalBill.setText(lblNetTotal.getText());
        lblDiscountBill.setText(lblDiscount.getText());

        tblBill.setItems(cartTMObservableList);


    }

    private void clearAll() throws SQLException, ClassNotFoundException {
        txtId.setEditable(false);
        txtName.setEditable(false);
        txtAddress.setEditable(false);
        cmbTitle.setDisable(true);
        txtCity.setEditable(false);
        cmbProvince.setDisable(true);
        txtPostalCode.setEditable(false);
        btnSave.setDisable(true);
        clearAllCustomerFields();
        clearAllItemFields();
        cmbCustIds.setDisable(false);
        tblCart.setItems(FXCollections.observableArrayList());
        lblNetTotal.setText("0.00");
        lblDiscount.setText("0.00");
        lblGrossTotal.setText("0.00");
    }

    public void openUserFormOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) placeOrderFormContext.getScene().getWindow();
        window.setY(250);
        window.setX(600);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/userForm.fxml"))));
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


    public void calculateChangeOnAction(ActionEvent actionEvent) {
        if (Validation.validate(mapCash)){
            boolean cashGreaterThanTotal = Double.parseDouble(lblNetTotalBill.getText())<=Double.parseDouble(txtCashBill.getText());
            if (cashGreaterThanTotal) {
                double change =Double.parseDouble(txtCashBill.getText())-Double.parseDouble(lblNetTotalBill.getText());
                lblChangeBill.setText(String.valueOf(new DecimalFormat("#.00").format(change)));
            }else {
                txtCashBill.setStyle("-fx-border-color: red;"+"-fx-border-width: 2");
            }
        }


    }

    private void clearBillDetails(){
        txtDateAndTimeBill.setText("");
        txtOrderIdBill.clear();
        txtCustomerIdBill.clear();
        txtCashBill.clear();
        lblNetTotalBill.setText("0.00");
        lblDiscountBill.setText("0.00");
        lblGrossTotalBill.setText("0.00");
        lblChangeBill.setText("0.00");
        cartTMObservableList.clear();
        tblBill.setItems(cartTMObservableList);
    }





}
