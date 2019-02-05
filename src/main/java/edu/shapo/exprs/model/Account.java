package edu.shapo.exprs.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Objects;

@DatabaseTable(tableName = "accounts")
public class Account implements Comparable<Account> {

    @DatabaseField(id = true, columnName = "id")
    private Long id;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private BigDecimal currentAmout;

    public Account() {
    }

    public Account(Long id, BigDecimal currentAmout) {
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

    @Override
    public int compareTo(Account another) {
        return (this.id > another.id) ? 1 : (this.id < another.id) ? -1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
