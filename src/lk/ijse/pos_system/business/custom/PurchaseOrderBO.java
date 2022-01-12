package lk.ijse.pos_system.business.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDTO;

import java.sql.SQLException;

public interface PurchaseOrderBO extends SuperBO {
    ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllItemCodes() throws SQLException, ClassNotFoundException;

    String generateNewOrderId() throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException;

    boolean addCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException;

    boolean placeOrder(OrderDTO orderDTO);

    public String generateNewCustomerId() throws SQLException, ClassNotFoundException;
}
