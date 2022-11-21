package ru.sorokin.model;

import lombok.Data;

@Data
public class StatisticBetweenDate {
    private int id;
    private Customer customer;
    private Product product;
    private Double sum;
}
