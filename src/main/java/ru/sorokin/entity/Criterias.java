package ru.sorokin.entity;

import lombok.Data;

import java.util.List;

@Data
public class Criterias {
    private String lastName;
    private String productName;
    private Integer minTimes;
    private Double minExpenses;
    private Double maxExpenses;
    private Integer badCustomers;
}