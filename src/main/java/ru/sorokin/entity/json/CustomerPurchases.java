package ru.sorokin.entity.json;

import lombok.Data;

import java.util.List;
@Data
public class CustomerPurchases {
    private final String name;
    private final List<ProductForStat> purchases;
    private final Double totalExpenses;
}
