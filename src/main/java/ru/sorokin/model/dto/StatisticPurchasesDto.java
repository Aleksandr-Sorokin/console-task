package ru.sorokin.model.dto;

import lombok.Data;
import ru.sorokin.enums.TypeResponse;
import ru.sorokin.model.CustomerPurchases;

import java.util.List;

@Data
public class StatisticPurchasesDto {
    private TypeResponse type;
    private Integer totalDays;
    private List<CustomerPurchases> customers;
    private Double totalExpenses;
    private Double avgExpenses;
}
