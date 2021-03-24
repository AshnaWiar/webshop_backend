package org.example.auth;

import io.dropwizard.auth.Authorizer;
import org.example.core.Account;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestContext;

public class JWTAuthorizer implements Authorizer<Account> {
    /**
     * @deprecated
     */
    @Override
    public boolean authorize(Account account, String role) {
        return false;
    }

    @Override
    public boolean authorize(Account principal, String role, @Nullable ContainerRequestContext requestContext) {
        return principal.role.equalsIgnoreCase(role);
    }
}
