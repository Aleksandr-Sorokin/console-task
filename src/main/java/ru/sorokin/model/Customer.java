package ru.sorokin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Customer {
    private Long id;
    private final String firstName;
    private final String lastName;
}
