package org.example.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import org.example.api.account.AccountRegistration;
import org.example.api.account.AccountRepresentation;
import org.example.api.auth.Credentials;
import org.example.core.Account;
import org.example.db.AccountDAO;
import org.example.service.AccountService;
import org.example.service.JWTService;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authenticate")
@Produces(MediaType.APPLICATION_JSON)
@Timed
public class AuthenticationResource {


    private final AccountDAO accountDAO;
    private final AccountService accountService;
    private final JWTService JWTService;

    public AuthenticationResource(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.accountService = new AccountService(accountDAO);
        this.JWTService = new JWTService();
    }

    @POST
    @Path("/login")
    @UnitOfWork
    public Response login(@NotNull @Valid Credentials credentials) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        Account account = this.accountDAO.findByUsername(credentials.username).orElseThrow(NotFoundException::new);
        System.out.println(account.password);

        if (!passwordEncryptor.checkPassword(credentials.password, account.password)) {
            throw new NotFoundException();
        }

        account.jwtToken = JWTService.generateToken();
        this.accountDAO.saveAccount(account).orElseThrow(BadRequestException::new);

        return Response.ok().entity(AccountRepresentation.createFromAccount(account)).build();
    }


    @POST
    @Path("/register")
    @UnitOfWork
    public Response register(@NotNull @Valid AccountRegistration accountRegistration) {
        Account account = this.accountService.registerAccount(accountRegistration);
        account.jwtToken = JWTService.generateToken();

        return Response.ok().entity(AccountRepresentation.createFromAccount(account)).build();
    }

}
