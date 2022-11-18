package ru.sorokin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor

public class CustomerPurchases {
    private Long id;
    private final Customer customer;
    private final List<Purchases> purchases;
    private final LocalDateTime dateTime;
}
