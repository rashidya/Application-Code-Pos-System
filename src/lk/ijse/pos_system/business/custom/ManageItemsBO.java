package lk.ijse.pos_system.business.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ManageItemsBO extends SuperBO {
    boolean addItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean updateItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean deleteItem(String text) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllItemCodes() throws SQLException, ClassNotFoundException;

    public String generateNewItemId() throws SQLException, ClassNotFoundException;
}
