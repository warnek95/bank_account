package com.carbonit.katas.bank.jarouna.formatter;

import com.carbonit.katas.bank.jarouna.Operation;
import com.carbonit.katas.bank.jarouna.OperationType;
import com.carbonit.katas.bank.jarouna.StatementFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public class GridStatementFormatter implements StatementFormatter {

    private static final String SEPARATOR = "||";
    private static final int NB_CHARS = 14;
    private static final int SCALE = 2;
    static final String HEADER = SEPARATOR +
            " date         " + SEPARATOR +
            " credit       " + SEPARATOR +
            " debit        " + SEPARATOR +
            " balance      " + SEPARATOR;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String format(List<Operation> operations, ZoneId zoneId) {
        StringBuilder stringBuilder = new StringBuilder(HEADER + System.lineSeparator());

        operations.stream()
                .sorted(Comparator.comparing(Operation::getDate).reversed())
                .map(operation -> format(operation, zoneId))
                .forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    private String format(Operation operation, ZoneId zoneId) {
        return formatDate(operation.getDate(), zoneId) +
                formatAmountGivenAnOperationType(operation, OperationType.DEPOSIT) +
                formatAmountGivenAnOperationType(operation, OperationType.WITHDRAWAL) +
                formatBigDecimal(operation.getBalance()) + SEPARATOR + System.lineSeparator();
    }

    private String formatDate(LocalDateTime date, ZoneId zoneId) {
        return formatStringWithPadding(date.atZone(UTC)
                .withZoneSameInstant(zoneId)
                .toLocalDateTime()
                .format(dateTimeFormatter));
    }

    private String formatStringWithPadding(String s) {
        return String.format(SEPARATOR + " %1$-" + (NB_CHARS-1) + "s", s);
    }

    private String formatAmountGivenAnOperationType(Operation operation, OperationType operationType) {
        return operationType.equals(operation.getOperationType()) ?
                formatBigDecimal(operation.getAmount()) : formatStringWithPadding("");
    }

    private String formatBigDecimal(BigDecimal bigDecimal){
        return formatStringWithPadding(bigDecimal.setScale(SCALE, RoundingMode.DOWN).toString());
    }
}
