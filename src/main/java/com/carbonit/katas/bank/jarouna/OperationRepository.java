package com.carbonit.katas.bank.jarouna;

import java.util.List;

public interface OperationRepository {
    void add(Operation operation);

    List<Operation> findAll();
}
