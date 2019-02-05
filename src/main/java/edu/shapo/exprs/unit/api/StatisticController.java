package edu.shapo.exprs.unit.api;

import com.google.inject.Inject;
import edu.shapo.exprs.unit.service.AccountService;
import edu.shapo.exprs.unit.service.TransactionLogService;

public class StatisticController {

    @Inject
    private AccountService accountService;

    @Inject
    private TransactionLogService transactionLogService;

    public String getAllAccountsStatus() {
        return accountService.allAccountStatus();
    }

    public String getTransactionCount() {
        return String.valueOf(transactionLogService.getTransactionCount());
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setTransactionLogService(TransactionLogService transactionLogService) {
        this.transactionLogService = transactionLogService;
    }
}
