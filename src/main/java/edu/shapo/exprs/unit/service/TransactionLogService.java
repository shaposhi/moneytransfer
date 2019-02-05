package edu.shapo.exprs.unit.service;

import edu.shapo.exprs.model.TransactionLog;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionLogService {
    TransactionLog createAndSave(Long src, Long dst, BigDecimal amount, String initiator);
    Integer getTransactionCount();
    List<TransactionLog> getTransactionsFromSrc(Long srcId);
    List<TransactionLog> getTransactionsFromDst(Long dstId);

}
