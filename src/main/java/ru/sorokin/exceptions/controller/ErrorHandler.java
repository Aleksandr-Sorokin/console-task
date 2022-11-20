package ru.sorokin.exceptions.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sorokin.enums.TypeResponse;
import ru.sorokin.exceptions.model.ErrorDto;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ErrorHandler {
    private final ObjectMapper objectMapper;

    public void createErrorFile(String message) {
        ErrorDto errorDto = new ErrorDto(TypeResponse.ERROR, message);
        try {
            String error = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorDto);
            FileWriter writer = new FileWriter("outputError.json", true);
            writer.write(LocalDateTime.now() + "\n" + error + ", \n");
            writer.close();
            System.out.println(error);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
