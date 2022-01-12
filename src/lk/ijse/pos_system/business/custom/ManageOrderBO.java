package lk.ijse.pos_system.business.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.dto.OrderDetailsDTO;

import java.sql.SQLException;

public interface ManageOrderBO extends SuperBO {
    ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllItemCodes() throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllOrderIdsOfACustomer(String newValue) throws SQLException, ClassNotFoundException;

    ObservableList<OrderDetailsDTO> getItemsOfAnOrder(String newValue) throws SQLException, ClassNotFoundException;

    boolean updateOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException;

    boolean deleteOrder(String value) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException;
}
