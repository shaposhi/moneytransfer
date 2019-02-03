package edu.shapo.exprs.service;

import com.google.inject.Inject;
import edu.shapo.exprs.dao.AccountDao;
import edu.shapo.exprs.exception.MoneyTransferException;
import edu.shapo.exprs.model.Account;
import edu.shapo.exprs.model.Constant;
import edu.shapo.exprs.model.ErrorCode;
import edu.shapo.exprs.model.TransferStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TransferServiceImpl implements TransferService {

    private static final Logger log = LogManager.getLogger(TransferServiceImpl.class);

    @Inject
    private AccountDao accountDao;

    @Override
    public TransferStatus makeTransfer(Long srcId, Long dstId, BigDecimal amount, String initiator) throws MoneyTransferException {
        log.info("Received transfer request from: " + srcId + " to: " + dstId + " with amount: " + amount + " initiated by: " +initiator);


        List<Account> accounts = accountDao.getAllAccounts();

        Optional<Account> srcOpt = accounts.stream().filter(a -> a.getId().equals(srcId)).findFirst();
        Optional<Account> dstOpt = accounts.stream().filter(a -> a.getId().equals(dstId)).findFirst();

        if (!srcOpt.isPresent()) {
            log.error("Scr account not present for : " + srcId);
            throw new MoneyTransferException(ErrorCode.ERROR_002.name());
        }
        if (!dstOpt.isPresent()) {
            log.error("Dst account not present for : " + srcId);
            throw new MoneyTransferException(ErrorCode.ERROR_003.name());
        }

        processTransferring(srcOpt.get(), dstOpt.get(), amount);


        if (accounts != null) {
            accounts.forEach(a -> {
                log.info(a.toString());
            });
        }

        return new TransferStatus(Constant.TRANSFER_SUCCESSFUL, "transfer successful", null);
    }

    private void processTransferring(Account srcAcc, Account dstAcc, BigDecimal amount) throws MoneyTransferException {

        Account former, latter;
        if (srcAcc.compareTo(dstAcc) < 0) {
            former = srcAcc;
            latter = dstAcc;
        } else {
            former = dstAcc;
            latter = srcAcc;
        }
        synchronized (former) {
            synchronized (latter) {
                if (amount.compareTo(srcAcc.getCurrentAmout()) > 0) {
                    log.error("Scr account not have enough money : " + amount);
                    throw new MoneyTransferException(ErrorCode.ERROR_004.name());
                }
                srcAcc.setCurrentAmout(srcAcc.getCurrentAmout().subtract(amount));
                dstAcc.setCurrentAmout(dstAcc.getCurrentAmout().add(amount));
            }
        }

    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
