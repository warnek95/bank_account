package com.carbonit.katas.bank.jarouna;

import java.time.ZoneId;
import java.util.List;

public interface StatementFormatter {
    String format(List<Operation> operations, ZoneId zoneId);
}
