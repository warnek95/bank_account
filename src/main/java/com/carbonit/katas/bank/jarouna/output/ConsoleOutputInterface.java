package com.carbonit.katas.bank.jarouna.output;

import com.carbonit.katas.bank.jarouna.OutputInterface;

public class ConsoleOutputInterface implements OutputInterface {
    @Override
    public void output(String message) {
        System.out.print(message);
    }
}
