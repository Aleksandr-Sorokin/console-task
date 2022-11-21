package ru.sorokin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.sorokin.enums.TypeRequest;
import ru.sorokin.exceptions.controller.ErrorHandler;
import ru.sorokin.service.CreateFileService;
import ru.sorokin.service.ReadFileService;

import java.io.File;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final ReadFileService readFileService;
    private final CreateFileService createFileService;
    private final ErrorHandler errorHandler;

    @Override
    public void run(String... args) {
        if (args.length < 1) {
            errorHandler.createErrorFile("Добавьте файл с данными и повторите команду");
            System.out.println("\nДобавьте файл с данными и повторите команду\n");
            return;
        }
        if (args.length == 1) {
            String input = String.valueOf(args[0]);
            File fileInput = new File(input);
            File fileOutput = new File("output.json");
            workWithFile(fileInput, fileOutput);
        } else if (args.length > 1) {
            String input = String.valueOf(args[0]);
            String output = String.valueOf(args[1]);
            if (input.startsWith("input") && output.endsWith(".json") && !output.equals("input.json")) {
                File fileInput = new File(input);
                File fileOutput = new File(output);
                workWithFile(fileInput, fileOutput);
            } else if (output.equals("input.json") && !input.equals("input.json")) {
                File fileInput = new File(output);
                File fileOutput = new File(input);
                workWithFile(fileInput, fileOutput);
            } else {
                errorHandler.createErrorFile("Проверьте названия и формат файлов input.json и output.json");
                System.out.println("\nПроверьте названия и формат файлов input.json и output.json\n");
            }
        } else {
            errorHandler.createErrorFile("Что-то пошло не так!!!");
            System.out.println("\nЧто-то пошло не так!!!\n");
        }
    }

    private void workWithFile(File fileInput, File fileOutput) {
        String line = readFileService.loadFromFile(fileInput);
        Map<TypeRequest, String> result = readFileService.parseStringToJson(line);
        if (result.containsKey(TypeRequest.CRITERIAS)) {
            String d = createFileService.createFileCriteria(readFileService
                    .parseCriterias(result.get(TypeRequest.CRITERIAS)), fileOutput);
            System.out.println(d);
            System.out.println("\nФайл создан он находится по адресу - " + fileOutput.getAbsolutePath() + "\n");
        } else if (result.containsKey(TypeRequest.STAT)) {
            String d = createFileService.createFileStat(readFileService.parseStatistics(result.get(TypeRequest.STAT)), fileOutput);
            System.out.println(d);
            System.out.println("\nФайл создан он находится по адресу - " + fileOutput.getAbsolutePath() + "\n");
        }
    }
}
