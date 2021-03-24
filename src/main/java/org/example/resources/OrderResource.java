package org.example.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.example.api.order.OrderRegistration;
import org.example.api.order.OrderRepresentation;
import org.example.api.order.OrderStatusChangeRequest;
import org.example.core.Account;
import org.example.core.Order;
import org.example.enums.AccountRole;
import org.example.service.OrderService;
import org.example.service.ProductService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(AccountRole.ADMIN)
public class OrderResource {

    private final OrderService orderService;
    private final ProductService productService;

    public OrderResource(
            OrderService orderService,
            ProductService productService
    ) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @GET
    @UnitOfWork
    public Response getAll() {
        List<Order> orders = this.orderService
                .listOrders()
                .stream()
                .peek(OrderRepresentation::createFromOrder)
                .collect(Collectors.toList());

        return Response.ok().entity(
                orders
        ).build();
    }

    @GET
    @Path("/{orderId}")
    @UnitOfWork
    @PermitAll
    public Response get(@PathParam("orderId") String orderId, @Auth Account account) {
        Order order = this.orderService.getOrderById(orderId);

        if(!order.account.equals(account)){
            throw new NotAuthorizedException("order belongs to an other account");
        }

        return Response.ok().entity(
                OrderRepresentation.createFromOrder(order)
        ).build();
    }

    @POST
    @UnitOfWork
    @PermitAll
    public Response store(@NotNull @Valid OrderRegistration orderRegistration, @Auth Account account) {
        Order order = Order.createFromRegistration(account, orderRegistration);

        order.orderItems = order.orderItems
                .stream()
                .peek(orderItem -> orderItem.setProduct(this.productService.getProductById(orderItem.getProduct().id)))
                .collect(Collectors.toSet());

        order = this.orderService.createOrder(order);

        return Response.ok().entity(
                OrderRepresentation.createFromOrder(order)
        ).build();
    }

    @PUT
    @UnitOfWork
    public Response updateStatus(@NotNull @Valid OrderStatusChangeRequest orderStatusChangeRequest) {
        Order order = this.orderService.getOrderById(orderStatusChangeRequest.id);

        return Response.ok().entity(
                OrderRepresentation.createFromOrder(
                        this.orderService.updateOrderStatus(order, orderStatusChangeRequest.status)
                )).build();
    }

    @DELETE
    @Path("/{orderId}")
    @UnitOfWork
    public Response delete(@PathParam("orderId") String orderId) {
        this.orderService.deleteOrder(orderId);

        return Response.ok().build();
    }
}
