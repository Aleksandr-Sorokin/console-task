package ru.sorokin.model;

import lombok.Data;
import ru.sorokin.enums.TypeResponse;

import java.util.List;

@Data
public class StatisticPurchases {
    private Long id;
    private TypeResponse type;
    private Integer totalDays;
    private List<CustomerPurchases> customers;
    private Double totalExpenses;
    private Double avgExpenses;
}
