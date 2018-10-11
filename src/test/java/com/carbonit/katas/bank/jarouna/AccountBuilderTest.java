package com.carbonit.katas.bank.jarouna;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountBuilderTest {
    private static final ZoneId PARIS_ZONE_ID = ZoneId.of("Europe/Paris");
    private static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.of(2018, 10, 2, 12, 14, 0, 0, PARIS_ZONE_ID);
    private static final LocalDateTime LOCAL_DATE_TIME_UTC = LocalDateTime.of(2018, 10, 2, 10, 14);

    private OperationRepository operationRepository;
    private StatementFormatter statementFormatter;
    private OutputInterface outputInterface;

    @BeforeEach
    void set_up(){
        operationRepository = mock(OperationRepository.class);
        statementFormatter = mock(StatementFormatter.class);
        outputInterface = mock(OutputInterface.class);
    }

    @DisplayName("As a bank client i can create an account with an initial amount")
    @Test
    void given_an_initial_amount_expect_account() {
        Operation operation = new Operation(BigDecimal.valueOf(1000), LOCAL_DATE_TIME_UTC, OperationType.DEPOSIT, BigDecimal.valueOf(1000));
        Account account = new AccountBuilder()
                .withInitialAmount(BigDecimal.valueOf(1000))
                .build(operationRepository, outputInterface, statementFormatter, ZONED_DATE_TIME);

        verify(operationRepository).add(operation);
        verifyNoMoreInteractions(operationRepository);

        assertEquals(account.balance, BigDecimal.valueOf(1000));

    }

    @DisplayName("As a bank client i can create an account without any provision")
    @Test
    void given_no_initial_amount_expect_account() {
        Account account = new AccountBuilder().build(operationRepository, outputInterface, statementFormatter, ZONED_DATE_TIME);

        verifyNoMoreInteractions(operationRepository);

        assertEquals(account.balance, BigDecimal.ZERO);
    }

    @DisplayName("As a bank client i can't create an account with a negative initial amount")
    @Test
    void given_a_negative_initial_amount_expect_an_illegal_argument_exception() {
        Executable executable = () -> {
            new AccountBuilder()
                    .withInitialAmount(BigDecimal.valueOf(-1000))
                    .build(operationRepository, outputInterface, statementFormatter, ZONED_DATE_TIME);
        };

        assertThrows(IllegalArgumentException.class, executable);

    }
}
