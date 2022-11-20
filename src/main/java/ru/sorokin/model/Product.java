package ru.sorokin.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Product {
    private Long id;
    private String title;
    private Double price;
}
