package ru.sorokin.model.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Statistics {
    private LocalDate startDate;
    private LocalDate endDate;
}
