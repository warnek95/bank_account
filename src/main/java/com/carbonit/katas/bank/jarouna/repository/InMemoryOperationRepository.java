package com.carbonit.katas.bank.jarouna.repository;

import com.carbonit.katas.bank.jarouna.Operation;
import com.carbonit.katas.bank.jarouna.OperationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryOperationRepository implements OperationRepository {
    private final List<Operation> operations = new ArrayList<>();
    @Override
    public void add(Operation operation) {
        operations.add(operation);
    }

    @Override
    public List<Operation> findAll() {
        return Collections.unmodifiableList(operations);
    }
}
