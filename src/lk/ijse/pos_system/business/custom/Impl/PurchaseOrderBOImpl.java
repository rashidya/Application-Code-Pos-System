package lk.ijse.pos_system.business.custom.Impl;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.custom.PurchaseOrderBO;
import lk.ijse.pos_system.entity.Customer;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.entity.Order;
import lk.ijse.pos_system.entity.OrderDetail;
import lk.ijse.pos_system.repository.DAOFactory;
import lk.ijse.pos_system.repository.custom.CustomerDAO;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.repository.custom.OrderDAO;
import lk.ijse.pos_system.repository.custom.OrderDetailDAO;
import lk.ijse.pos_system.db.DbConnection;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.dto.OrderDetailsDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    private OrderDAO orderDAO= (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private ItemDAO itemDAO= (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);


    @Override
    public ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getAllCustomerIds();
    }

    @Override
    public ObservableList<String> getAllItemCodes() throws SQLException, ClassNotFoundException {
        return itemDAO.getAllItemCodes();
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewOrderId();
    }

    @Override
    public CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException {
        Customer search = customerDAO.search(id);
        return new CustomerDTO(search.getCustId(),search.getCustName(),search.getCustTitle(),search.getCustAddress(),search.getCity(),search.getProvince(),search.getPostalCode());
    }

    @Override
    public boolean addCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.add(new Customer(dto.getId(),dto.getName(),dto.getTitle(),dto.getAddress(), dto.getCity(),dto.getProvince(),dto.getPostalCode()));
    }

    @Override
    public ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException {
        Item search = itemDAO.search(code);
        return new ItemDTO(search.getItemCode(),search.getItem(),search.getDescription(),search.getPackSize(),search.getUnitPrice(),search.getDiscount(),search.getQtyOnHand());
    }

    @Override
    public boolean placeOrder(OrderDTO dto) {
        Connection con= null;
        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            Order order = new Order(dto.getOrderId(), LocalDate.parse(dto.getOrderDate()), dto.getCustomerId());
            boolean orderAdded = orderDAO.add(order);
            if(orderAdded){
                ArrayList<OrderDetailsDTO> orderItems = dto.getOrderItems();
                AddOrderItems:
                for (OrderDetailsDTO orderItem : orderItems) {
                    OrderDetail orderDetail = new OrderDetail(orderItem.getOrderId(), orderItem.getItemCode(), orderItem.getOrderQty(), orderItem.getDiscount());
                    if(orderDetailDAO.add(orderDetail)) {
                        boolean updateStock = itemDAO.updateStock(orderItem.getItemCode(), orderItem.getOrderQty(), "remove");
                        if (updateStock) {
                            continue AddOrderItems;
                        }else {
                            con.rollback();
                            return false;
                        }
                    }else{
                        con.rollback();
                        return false;
                    }
                }
                con.commit();
                return true;
            }else{
                con.rollback();
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public String generateNewCustomerId() throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewCustomerId();
    }
}
