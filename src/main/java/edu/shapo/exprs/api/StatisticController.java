package edu.shapo.exprs.api;

import com.google.inject.Inject;
import edu.shapo.exprs.service.AccountService;
import edu.shapo.exprs.service.AuditTransactionLogService;

public class StatisticController {

    @Inject
    private AccountService accountService;

    @Inject
    private AuditTransactionLogService auditTransactionLogService;

    public String getAllAccountsStatus() {
        return accountService.allAccountStatus();
    }

    public String getTransactionCount() {
        return String.valueOf(auditTransactionLogService.getTransactionCount());
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setAuditTransactionLogService(AuditTransactionLogService auditTransactionLogService) {
        this.auditTransactionLogService = auditTransactionLogService;
    }
}
