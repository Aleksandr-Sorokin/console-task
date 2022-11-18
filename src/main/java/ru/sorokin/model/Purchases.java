package ru.sorokin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Purchases {
    private final Product products;
    private final Integer volume;
}