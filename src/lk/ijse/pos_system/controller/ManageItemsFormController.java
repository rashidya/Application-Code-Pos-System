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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.custom.ManageItemsBO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.validation.Validation;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageItemsFormController {
    public AnchorPane manageItemsFormContext;
    public TextField txtQtyOnHand;
    public TextField txtDiscount;
    public TextField txtItemCode;
    public TextField txtCategory;
    public TextField txtPackSize;
    public TextField txtItem;
    public TextField txtUnitPrice;
    public TextField txtDescription;
    public JFXButton btnAddUpdate;
    public TableView<ItemDTO> tblItem;
    public TableColumn colItemCode;
    public TableColumn colCategory;
    public TableColumn colPackSize;
    public TableColumn colDescription;
    public TableColumn colItem;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colQtyOnHand;
    public Label lblTime;
    public Label lblDate;
    public JFXButton btnRemove;

    ManageItemsBO manageItemsBO = (ManageItemsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEMS);
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

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

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        storeValidations();

        try {
            txtItemCode.setText(manageItemsBO.generateNewItemId());
            loadItemTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //-------------------------Listeners-------------------
        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                btnRemove.setDisable(false);
                loadItemData(newValue);
                btnAddUpdate.setText("Update");
            }
        });

        for (TextField textField : map.keySet()) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    checkAllFields();
            });
        }
    }

    private void storeValidations() {
        map.put(txtItem, Validation.text);
        map.put(txtUnitPrice,Validation.decimalNumber);
        map.put(txtPackSize,Validation.number);
        map.put(txtDescription,Validation.text);
        map.put(txtDiscount,Validation.decimalNumber);
        map.put(txtQtyOnHand,Validation.number);
    }

    public void openAdminFormOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) manageItemsFormContext.getScene().getWindow();
        window.setY(250);
        window.setX(600);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminForm.fxml"))));
    }

    public void addOrUpdateItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {


        if(checkAllFields()){
            ItemDTO itemDTO =new ItemDTO(txtItemCode.getText(),txtItem.getText(),txtDescription.getText(),txtPackSize.getText(),Double.parseDouble(txtUnitPrice.getText()),Double.parseDouble(txtDiscount.getText()),Integer.parseInt(txtQtyOnHand.getText()));

            if (btnAddUpdate.getText().equals("Add")){
                if (manageItemsBO.addItem(itemDTO)){
                    new Alert(Alert.AlertType.INFORMATION,"Item has been Added Successfully").show();
                    loadItemTable();
                    clearAllFields();
                }else{
                    new Alert(Alert.AlertType.WARNING,"Something Wrong.Try Again").show();

                }


            }else {
                //--------update----------------------------------
                if (manageItemsBO.updateItem(itemDTO)){
                    new Alert(Alert.AlertType.INFORMATION,"Item has been Updated Successfully").show();
                    loadItemTable();
                    btnAddUpdate.setText("Add");
                    clearAllFields();
                }else{
                    new Alert(Alert.AlertType.WARNING,"Something Wrong.Try Again").show();

                }
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
               if (manageItemsBO.deleteItem(txtItemCode.getText())) {
                   new Alert(Alert.AlertType.INFORMATION,"Item Has Been Removed").show();
                   clearAllFields();
                   loadItemTable();
               }else{
                   new Alert(Alert.AlertType.INFORMATION,"Something went wrong.Try Again").show();

               }

           }
           btnRemove.setDisable(true);
       }

    private void loadItemData(ItemDTO itemDTO) {
        txtItemCode.setText(itemDTO.getCode());
        txtItem.setText(itemDTO.getItem());
        txtDescription.setText(itemDTO.getDescription());
        txtPackSize.setText(itemDTO.getPackSize());
        txtUnitPrice.setText(String.valueOf(itemDTO.getUnitPrice()));
        txtDiscount.setText(String.valueOf(itemDTO.getDiscount()));
        txtQtyOnHand.setText(String.valueOf(itemDTO.getQtyOnHand()));
    }

     void loadItemTable() throws SQLException, ClassNotFoundException {
        ObservableList<ItemDTO> allItems=FXCollections.observableArrayList();
         for (ItemDTO itemDTO : manageItemsBO.getAllItems()) {
             allItems.add(itemDTO);
         }
        tblItem.setItems(allItems);

    }

    private void clearAllFields() throws SQLException, ClassNotFoundException {

        txtItemCode.setText(manageItemsBO.generateNewItemId());
        for (TextField textField : map.keySet()) {
            textField.clear();
            textField.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");
        }
        txtItemCode.setStyle("-fx-border-color: silver;"+"-fx-border-width: 1");

    }

    private boolean checkAllFields(){
        /*if (txtItemCode.getText().isEmpty()||txtItem.getText().isEmpty()||txtPackSize.getText().isEmpty()||txtDescription.getText().isEmpty()||txtUnitPrice.getText().isEmpty()||txtDiscount.getText().isEmpty()||txtQtyOnHand.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING,"All fields should be filled").show();
            return false;
        }else{
            if(manageItemsBO.getAllItemCodes().contains(txtItem.getText()) && btnAddUpdate.getText().equals("Add")){
                new Alert(Alert.AlertType.WARNING,"Item Id Already Exists").show();
                return false;
            }else{
                if(txtPackSize.getText().matches("^([1-9][0-9]{1,})|([1-9]{1,})$")){
                    if(txtUnitPrice.getText().matches("^([1-9][0-9]{1,}[.][0-9]{1,})|([1-9][0-9]{1,})|([1-9]{1,}[.][0-9]{1,})|([1-9]{1,})$")){
                        if(txtDiscount.getText().matches("^([0-9]{1,}[.][0-9]{1,})|([0-9]{1,})$")){
                            if(txtQtyOnHand.getText().matches("^([1-9][0-9]{1,})|([1-9]{1,})$")){
                                    return true;
                            }else{
                                new Alert(Alert.AlertType.WARNING,"Invalid Qty").show();
                                return false;
                            }
                        }else {
                            new Alert(Alert.AlertType.WARNING,"Invalid Discount").show();
                            return false;
                        }
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Invalid Unit Price").show();
                        return false;
                    }

                }else{
                    new Alert(Alert.AlertType.WARNING,"Invalid pack Size").show();
                    return false;
                }
            }
        }*/

        return Validation.validate(map);


    }



}
