package ru.sorokin.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
}
