package ru.sorokin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.sorokin.enums.TypeRequest;
import ru.sorokin.service.CreateFileService;
import ru.sorokin.service.ReadFileService;

import java.io.File;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final ReadFileService readFileService;
    private final CreateFileService createFileService;

    @Override
    public void run(String... args) {
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
