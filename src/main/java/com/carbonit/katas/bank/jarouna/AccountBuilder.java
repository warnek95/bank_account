package com.carbonit.katas.bank.jarouna;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class AccountBuilder {

    static final String NOT_VALID_INITIAL_AMOUNT_ERR_MESSAGE = "Cannot create an account with a negative initial amount";
    private BigDecimal initialAmount = null;

    public AccountBuilder withInitialAmount(BigDecimal amount) {
        throwExceptionIfInitialAmountNotValid(amount);
        initialAmount = amount;
        return this;
    }

    public Account build(OperationRepository operationRepository, OutputInterface outputInterface, StatementFormatter statementFormatter, ZonedDateTime creationDateTime) {
        Account account = new Account(operationRepository, outputInterface, statementFormatter, BigDecimal.ZERO);
        if (initialAmount != null && !initialAmount.equals(BigDecimal.ZERO)) {
            account.deposit(initialAmount, creationDateTime);
        }
        return account;
    }

    private void throwExceptionIfInitialAmountNotValid(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(NOT_VALID_INITIAL_AMOUNT_ERR_MESSAGE);
        }
    }
}
