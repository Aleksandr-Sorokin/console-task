package ru.sorokin.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.sorokin.model.Customer;
import ru.sorokin.model.Product;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PurchasesDto {
    private final Customer customer;
    private final List<Product> products;
    private final LocalDateTime dateTime;
}
