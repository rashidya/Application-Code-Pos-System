package lk.ijse.pos_system.business.custom.Impl;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.custom.ManageItemsBO;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.repository.DAOFactory;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class ManageItemsBOImpl implements ManageItemsBO {
    private ItemDAO itemDAO= (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean addItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        return itemDAO.add(new Item(dto.getCode(),dto.getItem(),dto.getDescription(),dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount(),dto.getQtyOnHand()));
    }

    @Override
    public boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        return itemDAO.update(new Item(dto.getCode(),dto.getItem(),dto.getDescription(),dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount(),dto.getQtyOnHand()));
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(code);
    }

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> items  =new ArrayList<>();
        for (Item item : itemDAO.getAll()) {
            items.add(new ItemDTO(item.getItemCode(),item.getItem(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getDiscount(),item.getQtyOnHand()));
        }
        return items;
    }

    @Override
    public ObservableList<String> getAllItemCodes() throws SQLException, ClassNotFoundException {
        return itemDAO.getAllItemCodes();
    }

    public String generateNewItemId() throws SQLException, ClassNotFoundException{
        return itemDAO.generateNewItemId();
    }
}
