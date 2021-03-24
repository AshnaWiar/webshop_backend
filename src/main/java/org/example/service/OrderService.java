package org.example.service;

import org.example.core.Account;
import org.example.core.Order;
import org.example.core.Order.OrderStatus;
import org.example.db.OrderDAO;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public List<Order> listOrders() {
        return this.orderDAO.listAll();
    }

    public Order getOrderById(String id) {
        return this.orderDAO.findById(id).orElseThrow(NotFoundException::new);
    }

    public Order createOrder(Order order) {
        return this.orderDAO.createOrder(order).orElseThrow(BadRequestException::new);
    }

    public Order updateOrderStatus(Order order, OrderStatus orderStatus) {
        order.status = orderStatus;
        return this.orderDAO.updateStatus(order).orElseThrow(BadRequestException::new);
    }

    public void deleteOrder(String orderId) {
        this.orderDAO.delete(orderId);
    }

    public List<Order> getOrderByAccount(Account account) {
        return this.orderDAO.findByAccountId(account.id).orElseThrow(NotFoundException::new);
    }
}
