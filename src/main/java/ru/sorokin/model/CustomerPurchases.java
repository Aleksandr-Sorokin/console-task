package ru.sorokin.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomerPurchases {
    private String name; //lastName firstName  Customer.class
    private List<Purchases> purchases;
    private Double totalExpenses;
}
