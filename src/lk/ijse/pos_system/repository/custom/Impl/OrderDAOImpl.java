package lk.ijse.pos_system.repository.custom.Impl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.repository.CrudUtil;
import lk.ijse.pos_system.repository.custom.OrderDAO;
import lk.ijse.pos_system.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;


public class OrderDAOImpl implements OrderDAO {
    ObservableList<Order> orderList = FXCollections.observableArrayList();

    @Override
    public boolean add(Order dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO `Order` VALUES (?,?,?)",dto.getOrderId(),dto.getOrderDate(),dto.getCustomerId());
    }

    @Override
    public boolean update(Order dto) throws SQLException, ClassNotFoundException {
       return CrudUtil.executeUpdate("UPDATE `Order` SET orderDate=? WHERE orderId=?",dto.getOrderDate(),dto.getOrderId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
       return CrudUtil.executeUpdate("DELETE FROM `Order` WHERE orderId=?",id);
    }

    @Override
    public ArrayList<Order> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Order search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT orderId From `Order` ORDER BY orderId DESC LIMIT 1");
        if (resultSet.next()){
            int tempId=Integer.parseInt(resultSet.getString(1).split("-")[1]);
            tempId++;
            return  (tempId<10)?"O-00"+tempId:(tempId<100)?"O-0"+tempId:"O-"+tempId;
        }else {
            return "O-001";
        }
    }

    @Override
    public ObservableList<String> getAllOrderIdsOfACustomer(String id) throws SQLException, ClassNotFoundException {
        ObservableList<String> orderIdList = FXCollections.observableArrayList();
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE customerId=?", id);
        while (resultSet.next()){
            orderIdList.add(resultSet.getString(1));
        }
        return  orderIdList;
    }

    @Override
    public ObservableList<Order> getOrdersFromDate(String date) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE orderDate=?", date);
        while (resultSet.next()){
            Order orderDTO =new Order(resultSet.getString(1), LocalDate.parse(resultSet.getString(2)),resultSet.getString(3));
            orderList.add(orderDTO);
        }
        return orderList;
    }

    @Override
    public ObservableList<Order> getOrdersFromMonthOfYear(String month, int year) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE orderDate BETWEEN ? AND ?", year + "-" + month + "-01", year + "-" + month +"-31");
        while (resultSet.next()){
            Order orderDTO =new Order(resultSet.getString(1), LocalDate.parse(resultSet.getString(2)),resultSet.getString(3));
            orderList.add(orderDTO);
        }
        return orderList;

    }

    @Override
    public ObservableList<Order> getOrdersFromYear(int year) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE orderDate BETWEEN ? AND ?", year + "-01-01",year + "-12-31");
        while (resultSet.next()){
            Order orderDTO =new Order(resultSet.getString(1), LocalDate.parse(resultSet.getString(2)),resultSet.getString(3));
            orderList.add(orderDTO);
        }
        return orderList;
    }

    @Override
    public ObservableList<Order> getOrdersFromSeason(String fromDate, String toDate) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE orderDate BETWEEN ? AND ?", fromDate, toDate);
        while (resultSet.next()){
            Order orderDTO =new Order(resultSet.getString(1), LocalDate.parse(resultSet.getString(2)),resultSet.getString(3));
            orderList.add(orderDTO);
        }
        return orderList;
    }


   }
