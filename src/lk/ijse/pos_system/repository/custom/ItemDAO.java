package lk.ijse.pos_system.repository.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.repository.CrudDAO;
import lk.ijse.pos_system.entity.Item;

import java.sql.SQLException;

public interface ItemDAO extends CrudDAO<Item,String> {
    ObservableList<String> getAllItemCodes() throws SQLException, ClassNotFoundException;
    public boolean updateStock(String itemCode, int orderQty,String addOrRemove) throws SQLException, ClassNotFoundException;
    public String generateNewItemId() throws SQLException, ClassNotFoundException;
}
