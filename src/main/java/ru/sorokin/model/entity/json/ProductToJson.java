package ru.sorokin.model.entity.json;

import lombok.Data;

@Data
public class ProductToJson {
    private final String productName;
    private final Integer minTimes;
}
