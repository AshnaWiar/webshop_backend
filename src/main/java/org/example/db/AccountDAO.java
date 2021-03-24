package org.example.db;

import org.example.core.Account;
import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

public class AccountDAO extends AbstractDAO<Account> {

    public AccountDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Account> findById(String id) {
        return Optional.ofNullable(get(id));
    }

    @SuppressWarnings("unchecked")
    public List<Account> listAll() {
        return list((Query<Account>) namedQuery("org.example.core.Account.findAll"));
    }


    @SuppressWarnings("unchecked")
    public Optional<Account> findByToken(String token) {
        return list(
                (Query<Account>) namedQuery("org.example.core.Account.findByToken")
                        .setParameter("jwtToken", token)
        ).stream().findFirst();
    }

    @SuppressWarnings("unchecked")
    public Optional<Account> findByUsername(String username) {
        return list(
                (Query<Account>) namedQuery("org.example.core.Account.findByUsername")
                        .setParameter("username", username)
        ).stream().findFirst();
    }

    public Optional<Account> saveAccount(Account account) {

        try {
            return Optional.of(persist(account));
        } catch (Exception exception) {
            currentSession().clear();
        }

        return Optional.empty();
    }

    public void deleteAccount(String accountId) {
        currentSession().delete(findById(accountId).orElseThrow(NotFoundException::new));
    }
}
