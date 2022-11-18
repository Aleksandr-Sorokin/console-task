package ru.sorokin.exceptions.controller;

import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {
    public void createErrorFile(String message) {
        System.out.println("class ErrorHandler " + message);
    }
}
