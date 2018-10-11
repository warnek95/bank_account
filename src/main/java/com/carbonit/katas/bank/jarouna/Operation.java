package com.carbonit.katas.bank.jarouna;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Operation {
    private final BigDecimal amount;
    private final LocalDateTime date;
    private final OperationType operationType;
    private final BigDecimal balance;

    public Operation(BigDecimal amount, LocalDateTime date, OperationType operationType, BigDecimal balance) {
        this.amount = amount;
        this.date = date;
        this.operationType = operationType;
        this.balance = balance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation that = (Operation) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(date, that.date) &&
                operationType == that.operationType &&
                Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, date, operationType, balance);
    }
}
