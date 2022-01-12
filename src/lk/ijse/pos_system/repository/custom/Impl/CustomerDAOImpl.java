package lk.ijse.pos_system.repository.custom.Impl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.repository.CrudUtil;
import lk.ijse.pos_system.repository.custom.CustomerDAO;
import lk.ijse.pos_system.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public boolean add(Customer dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Customer VALUES (?,?,?,?,?,?,?)",dto.getCustId(),dto.getCustName(),dto.getCustTitle(),dto.getCustAddress(),dto.getCity(),dto.getProvince(),dto.getPostalCode());
    }

    @Override
    public boolean update(Customer customerDTO) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public boolean delete(String id) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> allCustomers=new ArrayList<>();
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer ");
        while (rst.next()){
            allCustomers.add(new Customer(rst.getString(1),rst.getString(2),rst.getString(3),rst.getString(4),rst.getString(5),rst.getString(6),rst.getString(7)));
        }
        return allCustomers;
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM Customer WHERE CustId =?", id);
        if (resultSet.next()){
            return new Customer(resultSet.getString(1),resultSet.getString(2), resultSet.getString(3),resultSet.getString(4),resultSet.getString(5), resultSet.getString(6),resultSet.getString(7));

        }
        return null;
    }


    @Override
    public ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        ObservableList<String> allIDs =FXCollections.observableArrayList();
        ResultSet resultSet = CrudUtil.executeQuery("SELECT CustId FROM Customer");
        while (resultSet.next()) {
            allIDs.add(resultSet.getString(1));
        }
        return allIDs;
    }

    @Override
    public String generateNewCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT CustId From Customer ORDER BY CustId DESC LIMIT 1");
        if (resultSet.next()){
            int tempId=Integer.parseInt(resultSet.getString(1).split("-")[1]);
            tempId++;
            return  (tempId<10)?"C-00"+tempId:(tempId<100)?"C-0"+tempId:"C-"+tempId;
        }else {
            return "C-001";
        }
    }
}
