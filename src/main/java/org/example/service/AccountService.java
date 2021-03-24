package org.example.service;

import org.example.api.account.AccountRegistration;
import org.example.api.account.AccountRepresentation;
import org.example.core.Account;
import org.example.db.AccountDAO;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account getAccountById(String accountId) {
        return this.accountDAO.findById(accountId).orElseThrow(NotFoundException::new);
    }

    public List<AccountRepresentation> getAllAccounts() {
        return this.accountDAO
                .listAll()
                .stream()
                .map(AccountRepresentation::createFromAccount)
                .collect(Collectors.toList());
    }

    public Account registerAccount(AccountRegistration accountRegistration) {


        accountRegistration.password = (new StrongPasswordEncryptor())
                .encryptPassword(accountRegistration.password);

        return this.accountDAO.saveAccount(
                Account.createFromRegistration(accountRegistration)
        ).orElseThrow(BadRequestException::new);
    }

    public Account saveAccount(Account account) {
        account.password = (new StrongPasswordEncryptor())
                .encryptPassword(account.password);
        return this.accountDAO.saveAccount(account).orElseThrow(BadRequestException::new);
    }

    public void deleteAccount(String accountId) {
        this.accountDAO.deleteAccount(accountId);
    }


}
