package lk.ijse.pos_system.repository.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.entity.OrderDetail;
import lk.ijse.pos_system.repository.CrudDAO;

import java.sql.SQLException;

public interface OrderDetailDAO extends CrudDAO<OrderDetail,String> {
    public ObservableList<OrderDetail> getItemsOfAnOrder(String orderId) throws SQLException, ClassNotFoundException;
    public boolean deleteItemsOfAnOrder(String orderId) throws SQLException, ClassNotFoundException;
}
