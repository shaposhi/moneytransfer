package edu.shapo.exprs.unit.service;

import com.google.inject.Inject;
import edu.shapo.exprs.dao.AccountDao;
import edu.shapo.exprs.model.Account;
import spark.utils.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.StringJoiner;

public class AccountServiceImpl implements AccountService {

    @Inject
    private AccountDao accountDao;

    @Override
    public List<Account> findAll() {
        return accountDao.getAllAccounts();
    }

    @Override
    public Account findById(Long id) {
        return accountDao.findById(id);
    }

    @Override
    public String allAccountStatus() {
        StringJoiner sj = new StringJoiner("; ");

        List<Account> accounts = findAll();
        if (!CollectionUtils.isEmpty(accounts)) {

            BigDecimal sum = accounts.stream().map(Account::getCurrentAmout).reduce(BigDecimal.ZERO, BigDecimal::add);
            sj.add("Total sum: " + sum);

            accounts.forEach(a -> sj.add(a.toString()));

        }
        return sj.toString();
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
