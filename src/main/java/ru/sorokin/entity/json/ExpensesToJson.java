package ru.sorokin.entity.json;

import lombok.Data;

@Data
public class ExpensesToJson {
    private final Integer minExpenses;
    private final Integer maxExpenses;
}
