package ru.sorokin.model;

import lombok.*;

import javax.persistence.*;

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
