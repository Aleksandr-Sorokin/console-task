package ru.sorokin;

import ru.sorokin.enums.TypeRequest;
import ru.sorokin.service.ReadFileService;

import java.io.File;
import java.util.Map;

public class Main {
    File file;
    static ReadFileService fileService;

    public static void main(String[] args) {
        fileService = new ReadFileService();
        String line = fileService.loadFromFile(new File("input1.json"));
        Map<TypeRequest, String> result = fileService.parseStringToJson(line);
        if (result.containsKey(TypeRequest.CRITERIAS)) {
            System.out.println(fileService.parseCriterias(result.get(TypeRequest.CRITERIAS)));
        } else if (result.containsKey(TypeRequest.STAT)) {
            System.out.println(fileService.parseStatistics(result.get(TypeRequest.STAT)));
        }
    }
}