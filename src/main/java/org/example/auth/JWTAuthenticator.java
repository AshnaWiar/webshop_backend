package org.example.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import org.example.core.Account;
import org.example.db.AccountDAO;
import org.example.service.JWTService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Arrays;
import java.util.Optional;

public class JWTAuthenticator implements Authenticator<String, Account> {

    private final JWTService jwtService;
    private final AccountDAO accountDAO;

    @Context
    private HttpServletRequest httpServletRequest;

    public JWTAuthenticator(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.jwtService = new JWTService();

    }

    @Override
    @UnitOfWork
    public Optional<Account> authenticate(String token) throws AuthenticationException {
        if (jwtService.isValid(token)) {
            return accountDAO.findByToken(token);
        }

        return failAuth();
    }

    private Optional<Account> failAuth() {
        return Optional.empty();
    }
}
