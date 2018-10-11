package com.carbonit.katas.bank.jarouna;

import com.carbonit.katas.bank.jarouna.formatter.GridStatementFormatter;
import com.carbonit.katas.bank.jarouna.output.ConsoleOutputInterface;
import com.carbonit.katas.bank.jarouna.repository.InMemoryOperationRepository;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

public class App 
{

    private static final ZoneId SYSTEM_DEFAULT_ZONE_ID = ZoneId.systemDefault();
    
    public static void main( String[] args )
    {

        OperationRepository operationRepository = new InMemoryOperationRepository();
        StatementFormatter statementFormatter = new GridStatementFormatter();
        OutputInterface outputInterface = new ConsoleOutputInterface();
        
        BigDecimal firstDepositAmount = BigDecimal.valueOf(1000);
        ZonedDateTime firstDepositDate = ZonedDateTime.of(2018, 10, 11, 0, 0, 0, 0, SYSTEM_DEFAULT_ZONE_ID);

        BigDecimal secondDepositAmount = BigDecimal.valueOf(2000);
        ZonedDateTime secondDepositDate = ZonedDateTime.of(2018, 10, 12, 0, 0,0, 0, SYSTEM_DEFAULT_ZONE_ID);

        BigDecimal firstWithdrawAmount = BigDecimal.valueOf(500);
        ZonedDateTime firstWithdrawDate = ZonedDateTime.of(2018, 10, 13, 0, 0,0, 0, SYSTEM_DEFAULT_ZONE_ID);
        
        ZonedDateTime creationDateTime = ZonedDateTime.of(2018, 10, 10, 0, 0, 0, 0, UTC);
        Account account = new AccountBuilder()
                .build(operationRepository, outputInterface, statementFormatter, creationDateTime);

        account.deposit(firstDepositAmount, firstDepositDate);
        account.deposit(secondDepositAmount, secondDepositDate);
        account.withdraw(firstWithdrawAmount, firstWithdrawDate);

        account.outputStatement(SYSTEM_DEFAULT_ZONE_ID);
    }

}
