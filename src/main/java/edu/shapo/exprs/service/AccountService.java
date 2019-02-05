package edu.shapo.exprs.service;

import edu.shapo.exprs.model.Account;

import java.util.List;

public interface AccountService {
    List<Account> findAll();
    Account findById(Long id);

    String allAccountStatus();
}
