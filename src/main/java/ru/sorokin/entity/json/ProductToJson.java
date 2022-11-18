package ru.sorokin.entity.json;

import lombok.Data;

@Data
public class ProductToJson {
    private final String productName;
    private final Integer minTimes;
}
