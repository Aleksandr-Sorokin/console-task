package ru.sorokin.model.entity;

import lombok.Data;

@Data
public class Criterias {
    private String lastName;
    private String productName;
    private Integer minTimes;
    private Integer minExpenses;
    private Integer maxExpenses;
    private Integer badCustomers;
}
