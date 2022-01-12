package lk.ijse.pos_system.business.custom.Impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.custom.ManageOrderBO;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.entity.Order;
import lk.ijse.pos_system.entity.OrderDetail;
import lk.ijse.pos_system.repository.DAOFactory;
import lk.ijse.pos_system.repository.custom.CustomerDAO;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.repository.custom.OrderDAO;
import lk.ijse.pos_system.repository.custom.OrderDetailDAO;
import lk.ijse.pos_system.db.DbConnection;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.dto.OrderDetailsDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ManageOrderBOImpl implements ManageOrderBO {
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
    public ObservableList<String> getAllOrderIdsOfACustomer(String id) throws SQLException, ClassNotFoundException {
        return orderDAO.getAllOrderIdsOfACustomer(id);
    }

    @Override
    public ObservableList<OrderDetailsDTO> getItemsOfAnOrder(String id) throws SQLException, ClassNotFoundException {
        ObservableList<OrderDetailsDTO> orderItems = FXCollections.observableArrayList();
        for (OrderDetail temp : orderDetailDAO.getItemsOfAnOrder(id)) {
            orderItems.add(new OrderDetailsDTO(temp.getOrderId(),temp.getItemCode(),temp.getOrderQty(),temp.getDiscount()));
        }
        return orderItems;
    }

    @Override
    public boolean updateOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            boolean orderUpdated = orderDAO.update(new Order(dto.getOrderId(), LocalDate.parse(dto.getOrderDate()),dto.getCustomerId()));
            if(orderUpdated){
                ObservableList<OrderDetail> previousItemsOfOrder = orderDetailDAO.getItemsOfAnOrder(dto.getOrderId());
                boolean orderItemsDeleted = orderDetailDAO.deleteItemsOfAnOrder(dto.getOrderId());
                if(orderItemsDeleted) {
                    L1:for (OrderDetail oI : previousItemsOfOrder) {
                        boolean updated = itemDAO.updateStock(oI.getItemCode(), oI.getOrderQty(), "add");
                        if (updated){
                            continue L1;
                        }else{
                            con.rollback();
                            return false;
                        }
                    }
                    add:for (OrderDetailsDTO orderItem : dto.getOrderItems()) {
                        boolean newOrderItemAdded = orderDetailDAO.add(new OrderDetail(orderItem.getOrderId(),orderItem.getItemCode(),orderItem.getOrderQty(),orderItem.getDiscount()));
                        if (newOrderItemAdded) {
                            boolean updateStock = itemDAO.updateStock(orderItem.getItemCode(), orderItem.getOrderQty(), "remove");
                            if (updateStock){
                                continue add;
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
            }else {
                con.rollback();
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
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
    public boolean deleteOrder(String id){

        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            ObservableList<OrderDetail> orderItemDetails = orderDetailDAO.getItemsOfAnOrder(id);
            boolean orderAndOrderItemsDeleted = orderDAO.delete(id);
            if(orderAndOrderItemsDeleted){
                for (OrderDetail temp:orderItemDetails
                ) {
                    boolean stockUpdated = itemDAO.updateStock(temp.getItemCode(), temp.getOrderQty(), "add");
                    if (stockUpdated){
                        continue;
                    } else {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
    public ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException {
        Item search = itemDAO.search(itemCode);
        return new ItemDTO(search.getItemCode(),search.getItem(),search.getDescription(),search.getPackSize(),search.getUnitPrice(),search.getDiscount(),search.getQtyOnHand());
    }
}
