package ru.sorokin;

import ru.sorokin.enums.TypeRequest;
import ru.sorokin.service.CreateFileService;
import ru.sorokin.service.ReadFileService;

import java.io.File;
import java.util.Map;

public class Main {
    File file;
    static ReadFileService readFileService;
    static CreateFileService createFileService;

    public static void main(String[] args) {
        readFileService = new ReadFileService();
        createFileService = new CreateFileService();
        String line = readFileService.loadFromFile(new File("input.json"));
        Map<TypeRequest, String> result = readFileService.parseStringToJson(line);
        if (result.containsKey(TypeRequest.CRITERIAS)) {
            System.out.println(readFileService.parseCriterias(result.get(TypeRequest.CRITERIAS)));

            createFileService.createFileCriteria(readFileService.parseCriterias(result.get(TypeRequest.CRITERIAS)));
        } else if (result.containsKey(TypeRequest.STAT)) {
            System.out.println(readFileService.parseStatistics(result.get(TypeRequest.STAT)));
        }
    }
}