package ru.sorokin.service;

import ru.sorokin.enums.TypeRequest;

import java.io.File;
import java.util.Map;

public interface ReadService {
    String loadFromFile(File file);

    Map<TypeRequest, String> parseStringToJson(String load);
}
