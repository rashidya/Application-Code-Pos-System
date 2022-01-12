package lk.ijse.pos_system.repository.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.repository.CrudDAO;
import lk.ijse.pos_system.entity.Customer;

import java.sql.SQLException;

public interface CustomerDAO extends CrudDAO<Customer,String> {

    ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;
    public String generateNewCustomerId() throws SQLException, ClassNotFoundException;
}
