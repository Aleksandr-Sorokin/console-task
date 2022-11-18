package ru.sorokin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
}
