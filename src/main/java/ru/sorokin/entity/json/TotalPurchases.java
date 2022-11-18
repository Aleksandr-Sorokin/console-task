package ru.sorokin.entity.json;

import lombok.Data;

import java.util.List;
@Data
public class TotalPurchases {
    private final Integer totalDays;
    private final List<CustomerPurchases> customers;
    private final Double totalExpenses;
    private final Double avgExpenses;
}
