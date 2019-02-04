package edu.shapo.exprs.to;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

@DatabaseTable(tableName = "accounts")
public class AccountTO {

    @DatabaseField(id = true, columnName = "id")
    private Long id;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private BigDecimal currentAmout;


    public AccountTO(Long id, BigDecimal currentAmout) {
        this.id = id;
        this.currentAmout = currentAmout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCurrentAmout() {
        return currentAmout;
    }

    public void setCurrentAmout(BigDecimal currentAmout) {
        this.currentAmout = currentAmout;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", currentAmout=" + currentAmout +
                '}';
    }

}
