package ru.sorokin.model.entity.json;

import lombok.Data;

@Data
public class ExpensesToJson {
    private final Integer minExpenses;
    private final Integer maxExpenses;
}
