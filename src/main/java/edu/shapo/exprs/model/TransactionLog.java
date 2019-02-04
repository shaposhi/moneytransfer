package edu.shapo.exprs.model;

import java.math.BigDecimal;
import java.util.Date;

public final class TransactionLog {
    private final Long id;
    private final Long src;
    private final Long dst;
    private final BigDecimal amount;
    private final String initiator;
    private final Date createdDate;

    public TransactionLog(Long id, Long src, Long dst, BigDecimal amount, String initiator) {
        this.id = id;
        this.src = src;
        this.dst = dst;
        this.amount = amount;
        this.initiator = initiator;
        this.createdDate = new Date();
    }


    public Long getId() {
        return id;
    }

    public Long getSrc() {
        return src;
    }

    public Long getDst() {
        return dst;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getInitiator() {
        return initiator;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
