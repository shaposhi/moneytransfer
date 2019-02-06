package edu.shapo.exprs.dao;

import edu.shapo.exprs.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    List<Account> getAllAccounts();
    Optional<Account> findById(Long id);
    void closeConnection();
}
