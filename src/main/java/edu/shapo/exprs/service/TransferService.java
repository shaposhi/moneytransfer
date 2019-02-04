package edu.shapo.exprs.service;

import edu.shapo.exprs.exception.MoneyTransferException;
import edu.shapo.exprs.model.TransferStatus;

import java.math.BigDecimal;

public interface TransferService {
    TransferStatus makeTransfer(Long srcId, Long dstId, BigDecimal amount, String initiator) throws MoneyTransferException;
}
