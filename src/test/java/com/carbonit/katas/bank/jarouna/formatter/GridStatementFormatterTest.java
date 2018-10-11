package com.carbonit.katas.bank.jarouna.formatter;

import com.carbonit.katas.bank.jarouna.Operation;
import com.carbonit.katas.bank.jarouna.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.carbonit.katas.bank.jarouna.formatter.GridStatementFormatter.HEADER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GridStatementFormatterTest {

    private GridStatementFormatter gridStatementFormatter;
    private final List<Operation> EMPTY_OPERATION_LIST = Collections.emptyList();
    private static final ZoneId PARIS_ZONE_ID = ZoneId.of("Europe/Paris");

    @BeforeEach
    void set_up() {
        gridStatementFormatter = new GridStatementFormatter();
    }

    @DisplayName("A client will only get the header when asking for the statement of an account with no operations")
    @Test
    void given_no_operations_expect_only_the_header() {
        String expected = HEADER + System.lineSeparator();
        String formattedStatement = gridStatementFormatter.format(EMPTY_OPERATION_LIST, PARIS_ZONE_ID);

        assertEquals(expected, formattedStatement);
    }

    @DisplayName("A client will get the operations in a reverse chronological order when asking for the statement of an account with some operations")
    @Test
    void given_some_operations_expect_the_header_and_the_formatted_operations() {
        String expected = HEADER + System.lineSeparator() +
                "|| 14/10/2018   ||              || 500.00       || 2500.00      ||" + System.lineSeparator() +
                "|| 13/10/2018   || 2000.00      ||              || 3000.00      ||" + System.lineSeparator() +
                "|| 10/10/2018   || 1000.00      ||              || 1000.00      ||" + System.lineSeparator();

        LocalDateTime baseDateTime = LocalDateTime.of(2018, 10, 10, 0, 0, 0);
        List<Operation> operations = new ArrayList<>();
        operations.add(new Operation(BigDecimal.valueOf(1000), baseDateTime, OperationType.DEPOSIT, BigDecimal.valueOf(1000)));
        operations.add(new Operation(BigDecimal.valueOf(2000), baseDateTime.plusDays(3), OperationType.DEPOSIT, BigDecimal.valueOf(3000)));
        operations.add(new Operation(BigDecimal.valueOf(500), baseDateTime.plusDays(4), OperationType.WITHDRAWAL, BigDecimal.valueOf(2500)));

        String formattedStatement = gridStatementFormatter.format(operations, PARIS_ZONE_ID);

        assertEquals(expected, formattedStatement);
    }
}
