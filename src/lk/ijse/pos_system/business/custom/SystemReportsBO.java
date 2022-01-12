package lk.ijse.pos_system.business.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.dto.OrderDetailsDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SystemReportsBO extends SuperBO {
    ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;

    ObservableList<OrderDTO> getOrdersFromDate(String format) throws SQLException, ClassNotFoundException;

    ArrayList<OrderDetailsDTO> getItemsOfAnOrder(String orderId) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;
    ObservableList<OrderDTO> getOrdersFromMonthOfYear(String value, int newValue) throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String customer) throws SQLException, ClassNotFoundException;

    ObservableList<OrderDTO> getOrdersFromYear(int newValue) throws SQLException, ClassNotFoundException;

    ObservableList<OrderDTO> getOrdersFromSeason(String format, String format1) throws SQLException, ClassNotFoundException;
}
