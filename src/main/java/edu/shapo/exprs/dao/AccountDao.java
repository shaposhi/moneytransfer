package edu.shapo.exprs.dao;

import edu.shapo.exprs.model.Account;

import java.util.List;

public interface AccountDao {
    List<Account> getAllAccounts();
    Account findById(Long id);
    void closeConnection();
}
