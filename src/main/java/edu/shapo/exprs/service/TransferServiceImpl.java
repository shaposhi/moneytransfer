package edu.shapo.exprs.service;

import com.google.inject.Inject;
import edu.shapo.exprs.exception.MoneyTransferException;
import edu.shapo.exprs.model.Account;
import edu.shapo.exprs.model.ErrorCode;
import edu.shapo.exprs.model.TransferStatus;
import edu.shapo.exprs.model.TransferStatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferServiceImpl implements TransferService {

    private static final Logger log = LogManager.getLogger(TransferServiceImpl.class);

    private static final Map<Long, Lock> lockMap = new ConcurrentHashMap<>();

    @Inject
    private AccountService accountService;

    @Inject
    private AuditTransactionLogService auditTransactionLogService;

    @Override
    public TransferStatus makeTransfer(Long srcId, Long dstId, BigDecimal amount, String initiator) throws MoneyTransferException {
        log.debug("Received transfer request from: " + srcId + " to: " + dstId + " with amount: " + amount + " initiated by: " +initiator);

        Account src = accountService.findById(srcId);
        Account dst = accountService.findById(dstId);

        if (src == null) {
            log.error("Scr account not present for : " + srcId);
            throw new MoneyTransferException(ErrorCode.ERROR_002.name());
        }
        if (dst == null) {
            log.error("Dst account not present for : " + dstId);
            throw new MoneyTransferException(ErrorCode.ERROR_003.name());
        }

        processTransferring(src, dst, amount, initiator);

        return new TransferStatus(TransferStatusCode.SUCCESSFUL, "transfer successful", null);
    }

    private void processTransferring(Account srcAcc, Account dstAcc, BigDecimal amount, String initiator) throws MoneyTransferException {
        Lock former;
        Lock latter;
        if (srcAcc.compareTo(dstAcc) < 0) {
            former = getLockForAccountById(srcAcc.getId());
            latter = getLockForAccountById(dstAcc.getId());
        } else {
            former = getLockForAccountById(dstAcc.getId());
            latter = getLockForAccountById(srcAcc.getId());
        }

        try {
            former.lock();
            latter.lock();

            doTransfer(srcAcc, dstAcc, amount, initiator);

        } finally {
            latter.unlock();
            former.unlock();
        }
    }

    private void doTransfer(Account srcAcc, Account dstAcc, BigDecimal amount, String initiator) throws MoneyTransferException {
        if (amount.compareTo(srcAcc.getCurrentAmout()) > 0) {
            log.error("Scr account not have enough money : " + amount);
            throw new MoneyTransferException(ErrorCode.ERROR_004.name());
        }
        log.debug("Before: Source " + srcAcc + " dest: " + dstAcc);
        srcAcc.setCurrentAmout(srcAcc.getCurrentAmout().subtract(amount));
        dstAcc.setCurrentAmout(dstAcc.getCurrentAmout().add(amount));
        log.debug("After: Source " + srcAcc + " dest: " + dstAcc);
        auditTransactionLogService.createAndSave(srcAcc.getId(), dstAcc.getId(), amount, initiator, srcAcc.getCurrentAmout());
    }

    private Lock getLockForAccountById(Long accountId) {
        return lockMap.computeIfAbsent(accountId, k -> new ReentrantLock(true));
    }


    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setAuditTransactionLogService(AuditTransactionLogService auditTransactionLogService) {
        this.auditTransactionLogService = auditTransactionLogService;
    }
}
