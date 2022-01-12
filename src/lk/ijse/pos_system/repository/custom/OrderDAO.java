package lk.ijse.pos_system.repository.custom;


import javafx.collections.ObservableList;
import lk.ijse.pos_system.repository.CrudDAO;
import lk.ijse.pos_system.entity.Order;

import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order,String> {
    public String generateNewOrderId() throws SQLException, ClassNotFoundException;
    public ObservableList<String> getAllOrderIdsOfACustomer(String custId) throws SQLException, ClassNotFoundException;
    public ObservableList<Order> getOrdersFromDate(String date) throws SQLException, ClassNotFoundException;
    public ObservableList<Order> getOrdersFromMonthOfYear(String month, int year) throws SQLException, ClassNotFoundException;
    public ObservableList<Order> getOrdersFromYear(int year) throws SQLException, ClassNotFoundException;
    public ObservableList<Order> getOrdersFromSeason(String from, String to) throws SQLException, ClassNotFoundException;
}
