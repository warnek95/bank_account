package com.carbonit.katas.bank.jarouna;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

public class Account {

    private static final String AMOUNT_NEGATIVE_ERR_MESSAGE = "The amount of the operation can't be negative";
    private static final String NOT_ENOUGH_PROVISION_ERR_MESSAGE = "This account doesn't have enough provision for a withdrawal";
    private static final BigDecimal OVERDRAFT_LIMIT = BigDecimal.ZERO;

    private final OperationRepository operationRepository;
    private final OutputInterface outputInterface;
    private final StatementFormatter statementFormatter;
    BigDecimal balance;

    Account(OperationRepository operationRepository, OutputInterface outputInterface, StatementFormatter statementFormatter, BigDecimal balance) {
        this.operationRepository = operationRepository;
        this.outputInterface = outputInterface;
        this.statementFormatter = statementFormatter;
        this.balance = balance;
    }

    public void deposit(BigDecimal amount, ZonedDateTime zonedDateTime) {
        throwExceptionIfAmountNegative(amount);
        balance = balance.add(amount);

        LocalDateTime utcDateTime = zonedDateTime.withZoneSameInstant(UTC).toLocalDateTime();

        operationRepository.add(new Operation(amount, utcDateTime, OperationType.DEPOSIT, balance));
    }

    public void withdraw(BigDecimal amount, ZonedDateTime zonedDateTime) {
        throwExceptionIfAmountNegative(amount);
        throwExceptionIfAccountHasNoProvisionLeft(amount);
        balance = balance.subtract(amount);

        LocalDateTime utcDateTime = zonedDateTime.withZoneSameInstant(UTC).toLocalDateTime();

        operationRepository.add(new Operation(amount, utcDateTime, OperationType.WITHDRAWAL, balance));
    }

    private void throwExceptionIfAmountNegative(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(AMOUNT_NEGATIVE_ERR_MESSAGE);
        }
    }

    private void throwExceptionIfAccountHasNoProvisionLeft(BigDecimal amount) {
        if (balance.subtract(amount).compareTo(OVERDRAFT_LIMIT) < 0) {
            throw new IllegalStateException(NOT_ENOUGH_PROVISION_ERR_MESSAGE);
        }
    }

    public void outputStatement(ZoneId zoneId) {
        outputInterface.output(statementFormatter.format(operationRepository.findAll(), zoneId));
    }
}
