package edu.shapo.exprs.unit.service;


import edu.shapo.exprs.model.TransactionLog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TransactionLogServiceImpl implements TransactionLogService {

    private static Queue<TransactionLog> transactionLogs = new ConcurrentLinkedQueue<>();

    private static AtomicLong indexSequence = new AtomicLong(1L);

    @Override
    public TransactionLog createAndSave(Long src, Long dst, BigDecimal amount, String initiator) {
        Long id = indexSequence.getAndIncrement();
        TransactionLog log = new TransactionLog(id, src, dst, amount, initiator);
        transactionLogs.add(log);
        return log;
    }

    @Override
    public Integer getTransactionCount() {
        return transactionLogs.size();
    }

    @Override
    public List<TransactionLog> getTransactionsFromSrc(Long srcId) {
        return transactionLogs.stream().filter(log -> log.getSrc().equals(srcId)).collect(Collectors.toList());
    }

    @Override
    public List<TransactionLog> getTransactionsFromDst(Long dstId) {
        return transactionLogs.stream().filter(log -> log.getDst().equals(dstId)).collect(Collectors.toList());
    }
}
