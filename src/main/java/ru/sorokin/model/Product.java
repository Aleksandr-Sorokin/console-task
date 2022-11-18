package ru.sorokin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Product {
    private Long id;
    private String title;
    private Double price;
}
