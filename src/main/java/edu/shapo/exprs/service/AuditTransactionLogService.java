package edu.shapo.exprs.service;

import edu.shapo.exprs.model.TransactionLog;

import java.math.BigDecimal;
import java.util.List;

public interface AuditTransactionLogService {
    TransactionLog createAndSave(Long src, Long dst, BigDecimal amount, String initiator, BigDecimal srcAmountAfter);
    Integer getTransactionCount();
    List<TransactionLog> getTransactionsFromSrc(Long srcId);
    List<TransactionLog> getTransactionsFromDst(Long dstId);

}
