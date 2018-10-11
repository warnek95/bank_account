package com.carbonit.katas.bank.jarouna;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class AccountTest {

    private static final ZoneId PARIS_ZONE_ID = ZoneId.of("Europe/Paris");
    private static final ZonedDateTime BASE_ZONED_DATE_TIME = LocalDateTime.of(2018, 10, 2, 12, 14)
            .atZone(PARIS_ZONE_ID);
    private static final LocalDateTime BASE_DATE_TIME_UTC = LocalDateTime.of(2018, 10, 2, 10, 14);

    private OperationRepository operationRepository;
    private StatementFormatter statementFormatter;
    private OutputInterface outputInterface;

    private Account account;
    private final List<Operation> EMPTY_OPERATION_LIST = Collections.emptyList();

    @BeforeEach
    void set_up(){
        operationRepository = mock(OperationRepository.class);
        statementFormatter = mock(StatementFormatter.class);
        outputInterface = mock(OutputInterface.class);
        account = new Account(operationRepository, outputInterface, statementFormatter, BigDecimal.valueOf(1000));
    }

    @DisplayName("As a bank client I can make a deposit in my account")
    @Test
    void given_a_valid_amount_expect_the_deposit_operation_to_be_added_to_the_repository() {
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal balance = BigDecimal.valueOf(1100);
        Operation operation = new Operation(amount, BASE_DATE_TIME_UTC, OperationType.DEPOSIT, balance);

        account.deposit(amount, BASE_ZONED_DATE_TIME);

        verify(operationRepository).add(operation);
        verifyNoMoreInteractions(operationRepository);

        assertEquals(account.balance, balance);
    }

    @DisplayName("As a bank client I can't make a deposit in my account with a negative amount")
    @Test
    void given_a_negative_amount_for_a_deposit_expect_an_IllegalArgumentException() {
        Executable executable = () -> {
            BigDecimal amount = BigDecimal.valueOf(-100);

            account.deposit(amount, BASE_ZONED_DATE_TIME);
        };

        assertThrows(IllegalArgumentException.class, executable);
    }

    @DisplayName("As a bank client I can make a withdrawal from my account")
    @Test
    void given_a_valid_amount_expect_the_withdrawal_operation_to_be_added_to_the_repository() {
        BigDecimal amount = BigDecimal.valueOf(300);
        BigDecimal balance = BigDecimal.valueOf(700);
        Operation operation = new Operation(amount, BASE_DATE_TIME_UTC,
                OperationType.WITHDRAWAL, balance);

        account.withdraw(amount, BASE_ZONED_DATE_TIME);

        verify(operationRepository).add(operation);
        verifyNoMoreInteractions(operationRepository);

        assertEquals(account.balance, balance);
    }

    @DisplayName("As a bank client I can't make a withdrawal from my account if it doesn't have enough provision")
    @Test
    void given_not_enough_provision_for_a_withdrawal_expect_an_IllegalStateException() {
        Executable executable = () -> {
            BigDecimal amount = BigDecimal.valueOf(2000);

            account.withdraw(amount, BASE_ZONED_DATE_TIME);
        };

        assertThrows(IllegalStateException.class, executable);
    }

    @DisplayName("As a bank client I can't make a withdrawal from my account with a negative amount")
    @Test
    void given_a_negative_amount_for_a_withdrawal_expect_an_IllegalArgumentException() {
        Executable executable = () -> {
            BigDecimal amount = BigDecimal.valueOf(-100);

            account.withdraw(amount, BASE_ZONED_DATE_TIME);
        };

        assertThrows(IllegalArgumentException.class, executable);
    }

    @DisplayName("As a bank client I can see the statement of my account")
    @Test
    void given_an_account_expect_the_statement() {
        List<Operation> emptyOperationList = EMPTY_OPERATION_LIST;
        String expectedFormattedStatements = "";

        given(operationRepository.findAll()).willReturn(emptyOperationList);
        given(statementFormatter.format(anyList(), any(ZoneId.class))).willReturn(expectedFormattedStatements);

        InOrder order = Mockito.inOrder(operationRepository, statementFormatter, outputInterface);

        account.outputStatement(PARIS_ZONE_ID);

        order.verify(operationRepository).findAll();
        order.verify(statementFormatter).format(emptyOperationList, PARIS_ZONE_ID);
        order.verify(outputInterface).output(expectedFormattedStatements);

        verifyNoMoreInteractions(operationRepository);
        verifyNoMoreInteractions(statementFormatter);
        verifyNoMoreInteractions(outputInterface);
    }
}
