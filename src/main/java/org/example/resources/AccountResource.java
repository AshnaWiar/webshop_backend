package org.example.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.example.api.account.AccountRegistration;
import org.example.api.account.AccountRepresentation;
import org.example.api.order.OrderRepresentation;
import org.example.core.Account;
import org.example.core.Order;
import org.example.enums.AccountRole;
import org.example.service.AccountService;
import org.example.service.OrderService;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Timed
@RolesAllowed(AccountRole.ADMIN)
public class AccountResource {

    private final AccountService accountService;
    private final OrderService orderService;

    public AccountResource(
            AccountService accountService,
            OrderService orderService
    ) {
        this.accountService = accountService;
        this.orderService = orderService;
    }

    @GET
    @UnitOfWork
    public Response getAll() {
        return Response.ok().entity(this.accountService.getAllAccounts()).build();
    }

    @GET
    @Path("/{accountId}")
    @UnitOfWork
    public Response get(@PathParam("accountId") String accountId) {
        Account account = this.accountService.getAccountById(accountId);

        return Response.ok().entity(AccountRepresentation.createFromAccount(account)).build();
    }

    @GET
    @Path("/orders")
    @UnitOfWork
    @PermitAll
    public Response getAccountOrders(@Auth Account account) {
        List<Order> orders = this.orderService.getOrderByAccount(account);

        return Response.ok().entity(
                orders.stream().map(OrderRepresentation::createFromOrder).collect(Collectors.toList())
        ).build();
    }

    @POST
    @UnitOfWork
    public Response store(@NotNull @Valid AccountRegistration accountRegistration) {
        try {

            Account account = Account.createFromRegistration(accountRegistration);
            account = this.accountService.saveAccount(account);

            return Response.ok().entity(
                    AccountRepresentation.createFromAccount(account)
            ).build();
        } catch (ConstraintViolationException exception) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @UnitOfWork
    public Response put(@NotNull @Valid AccountRepresentation accountRepresentation, @Auth Account loggedInAccount) {
        try {
            Account account = this.accountService.getAccountById(accountRepresentation.id);
            account.updateFromRepresentation(accountRepresentation);

            // cannot change own role.
            if (account.equals(loggedInAccount)) {
                account.role = loggedInAccount.role;
            }

            account = this.accountService.saveAccount(account);

            return Response.ok().entity(
                    AccountRepresentation.createFromAccount(account)
            ).build();
        } catch (ConstraintViolationException exception) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{accountId}")
    @UnitOfWork
    public Response delete(@PathParam("accountId") String accountId) {
        this.accountService.deleteAccount(accountId);

        return Response.ok().build();
    }
}
