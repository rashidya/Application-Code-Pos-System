package lk.ijse.pos_system.repository.custom.Impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.repository.CrudUtil;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.db.DbConnection;
import lk.ijse.pos_system.entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Item dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Item VALUES (?,?,?,?,?,?,?)",dto.getItemCode(),dto.getItem(),dto.getDescription(),dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount()/100,dto.getQtyOnHand());
    }

    @Override
    public boolean update(Item dto) throws SQLException, ClassNotFoundException {
       return CrudUtil.executeUpdate("UPDATE Item SET Item=?,Description=?,PackSize=?,UnitPrice=?,Discount=?,QtyOnHand=? WHERE ItemCode=?",dto.getItem(),dto.getDescription(),dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount(),dto.getQtyOnHand(),dto.getItemCode());
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {

        return CrudUtil.executeUpdate("DELETE FROM Item WHERE ItemCode=?",code);
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Item> allItems = new ArrayList<>();
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item").executeQuery();
        while (resultSet.next()){
            Item itemDTO =new Item(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getDouble(5),resultSet.getDouble(6),resultSet.getInt(7));
            allItems.add(itemDTO);
        }
        return allItems;
    }

    @Override
    public Item search(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM Item WHERE ItemCode=?", code);
        if (resultSet.next()){
            Item itemDTO =new Item(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getDouble(5),resultSet.getDouble(6),resultSet.getInt(7));
            return itemDTO;
        }
        return null;
    }

    @Override
    public ObservableList<String> getAllItemCodes() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT ItemCode FROM Item");
        ObservableList<String> itemIdObservableList = FXCollections.observableArrayList();

        while (resultSet.next()){
            itemIdObservableList.add(resultSet.getString(1));
        }
        return itemIdObservableList;
    }

    @Override
    public boolean updateStock(String itemCode, int orderQty,String addOrRemove) throws SQLException, ClassNotFoundException {
        int newQtyOnHand=0;
        for (Item temp:getAll()
        ) {
            if(temp.getItemCode().equals(itemCode)){
                newQtyOnHand=(addOrRemove.equals("remove"))?temp.getQtyOnHand()-orderQty:temp.getQtyOnHand()+orderQty;
            }
        }
        return CrudUtil.executeUpdate("UPDATE Item SET QtyOnHand=? WHERE ItemCode=?",newQtyOnHand,itemCode);

    }

    @Override
    public String generateNewItemId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT ItemCode From Item ORDER BY ItemCode DESC LIMIT 1");
        if (resultSet.next()){
            int tempId=Integer.parseInt(resultSet.getString(1).split("-")[1]);
            tempId++;
            return  (tempId<10)?"I-00"+tempId:(tempId<100)?"I-0"+tempId:"I-"+tempId;
        }else {
            return "I-001";
        }
    }


}
