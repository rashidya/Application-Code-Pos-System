package lk.ijse.pos_system.repository.custom.Impl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.entity.OrderDetail;
import lk.ijse.pos_system.repository.CrudUtil;
import lk.ijse.pos_system.repository.custom.OrderDetailDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public boolean add(OrderDetail dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO `Order Detail` VALUES (?,?,?,?)", dto.getOrderId(), dto.getItemCode(), dto.getOrderQty(), dto.getDiscount());
    }

    @Override
    public boolean update(OrderDetail dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public OrderDetail search(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ObservableList<OrderDetail> getItemsOfAnOrder(String orderId) throws SQLException, ClassNotFoundException {
        ObservableList<OrderDetail>  orderItems = FXCollections.observableArrayList();
        ResultSet resultSet = CrudUtil.executeQuery("SELECT  * From `Order Detail` WHERE OrderId =?", orderId);
        while (resultSet.next()){
            OrderDetail temp =new OrderDetail(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getDouble(4));
            orderItems.add(temp);
        }
        return orderItems;
    }

    @Override
    public boolean deleteItemsOfAnOrder(String orderId) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("DELETE FROM `Order Detail` WHERE OrderId=?",orderId);

    }

}
