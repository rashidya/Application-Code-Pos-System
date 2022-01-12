package lk.ijse.pos_system.business.custom.Impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.custom.SystemReportsBO;
import lk.ijse.pos_system.entity.Customer;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.entity.Order;
import lk.ijse.pos_system.entity.OrderDetail;
import lk.ijse.pos_system.dto.OrderDetailsDTO;
import lk.ijse.pos_system.repository.DAOFactory;
import lk.ijse.pos_system.repository.custom.CustomerDAO;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.repository.custom.OrderDAO;
import lk.ijse.pos_system.repository.custom.OrderDetailDAO;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class SystemReportsBOImpl implements SystemReportsBO {
    private OrderDAO orderDAO= (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private ItemDAO itemDAO= (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);


    @Override
    public ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getAllCustomerIds();
    }

    @Override
    public ObservableList<OrderDTO> getOrdersFromDate(String date) throws SQLException, ClassNotFoundException {
        ObservableList<OrderDTO> orders = FXCollections.observableArrayList();
        for (Order order : orderDAO.getOrdersFromDate(date)) {
            orders.add(new OrderDTO(order.getOrderId(),order.getOrderDate().toString(),order.getCustomerId(),getItemsOfAnOrder(order.getOrderId())));
        }
        return orders;
    }

    @Override
    public ArrayList<OrderDetailsDTO> getItemsOfAnOrder(String orderId) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetailsDTO> itemsOfAnOrder= new ArrayList<>();
            for (OrderDetail temp : orderDetailDAO.getItemsOfAnOrder(orderId)) {
                itemsOfAnOrder.add(new OrderDetailsDTO(temp.getOrderId(),temp.getItemCode(),temp.getOrderQty(),temp.getDiscount()));
            }
            return itemsOfAnOrder;
    }

    @Override
    public ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException {
        Item search = itemDAO.search(itemCode);
        return new ItemDTO(search.getItemCode(),search.getItem(),search.getDescription(),search.getPackSize(),search.getUnitPrice(),search.getDiscount(),search.getQtyOnHand());

    }

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> allItems=new ArrayList<>();
        for (Item search : itemDAO.getAll()) {
            allItems.add(new ItemDTO(search.getItemCode(),search.getItem(),search.getDescription(),search.getPackSize(),search.getUnitPrice(),search.getDiscount(),search.getQtyOnHand()));


        }

       return allItems;
    }

    @Override
    public ObservableList<OrderDTO> getOrdersFromMonthOfYear(String month, int year) throws SQLException, ClassNotFoundException {
        ObservableList<OrderDTO> orders = FXCollections.observableArrayList();
        for (Order order :  orderDAO.getOrdersFromMonthOfYear(month, year)) {
            orders.add(new OrderDTO(order.getOrderId(),order.getOrderDate().toString(),order.getCustomerId(),getItemsOfAnOrder(order.getOrderId())));
        }
        return orders;
    }

    @Override
    public CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException {
        Customer search = customerDAO.search(id);
        return new CustomerDTO(search.getCustId(),search.getCustName(),search.getCustTitle(),search.getCustAddress(),search.getCity(),search.getProvince(),search.getPostalCode());

    }

    @Override
    public ObservableList<OrderDTO> getOrdersFromYear(int year) throws SQLException, ClassNotFoundException {
        ObservableList<OrderDTO> orders = FXCollections.observableArrayList();
        for (Order order :  orderDAO.getOrdersFromYear(year)) {
            orders.add(new OrderDTO(order.getOrderId(),order.getOrderDate().toString(),order.getCustomerId(),getItemsOfAnOrder(order.getOrderId())));
        }
        return orders;
    }

    @Override
    public ObservableList<OrderDTO> getOrdersFromSeason(String from, String to) throws SQLException, ClassNotFoundException {
        ObservableList<OrderDTO> orders = FXCollections.observableArrayList();
        for (Order order :  orderDAO.getOrdersFromSeason(from,to)) {
            orders.add(new OrderDTO(order.getOrderId(),order.getOrderDate().toString(),order.getCustomerId(),getItemsOfAnOrder(order.getOrderId())));
        }
        return orders;

    }
}
