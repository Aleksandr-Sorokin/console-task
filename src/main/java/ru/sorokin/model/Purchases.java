package ru.sorokin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Purchases {
    private Long id;
    private final Customer customer;
    private final List<Product> products;
    private final LocalDateTime dateTime;
}
