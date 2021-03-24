package org.example.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.example.core.Order;
import org.example.core.OrderItem;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderDAO extends AbstractDAO<Order> {

    public OrderDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Order> findById(String id) {
        return Optional.ofNullable(get(id));
    }

    @SuppressWarnings("unchecked")
    public List<Order> listAll() {
        return list((Query<Order>) namedQuery("org.example.core.Order.findAll"));
    }

    public Optional<Order> createOrder(Order order) {
        Set<OrderItem> orderItems = order.orderItems;

        // detach relations
        order.orderItems = null;

        Order databaseOrder = persist(order);

        databaseOrder.orderItems = orderItems
                .stream()
                .peek(orderItem -> orderItem.setOrder(databaseOrder))
                .collect(Collectors.toSet());

        return Optional.of(persist(databaseOrder));
    }

    public Optional<Order> updateStatus(Order order) {
        return Optional.of(persist(order));
    }

    public void delete(String orderId) {
        currentSession().delete(findById(orderId).orElseThrow(NotFoundException::new));
    }

    @SuppressWarnings("unchecked")
    public Optional<List<Order>> findByAccountId(String accountId) {
        return Optional.of(
                list((Query<Order>) namedQuery("org.example.core.Order.findByAccountId")
                        .setParameter("accountId", accountId)
                )
        );
    }
}
