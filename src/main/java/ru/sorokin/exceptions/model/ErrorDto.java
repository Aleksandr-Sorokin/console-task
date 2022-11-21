package ru.sorokin.exceptions.model;

import lombok.Data;
import ru.sorokin.enums.TypeResponse;

@Data
public class ErrorDto {
    private final TypeResponse type;
    private final String message;
}
