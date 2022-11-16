package ru.sorokin.service;

import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.sorokin.entity.Criterias;
import ru.sorokin.entity.Statistics;
import ru.sorokin.enums.TypeRequest;
import ru.sorokin.exceptions.controller.ErrorHandler;
import ru.sorokin.exceptions.model.CriteriaParseException;
import ru.sorokin.exceptions.model.StatisticParseException;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class ReadFileService implements ReadService {
    private ErrorHandler errorHandler = new ErrorHandler();

    public ReadFileService() {
    }

    @Override
    public String loadFromFile(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(file)));
            while (reader.ready()) {
                String line = reader.readLine().trim();
                builder.append(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            errorHandler.createErrorFile(String.format("Not found file %s",  e.getMessage()));
            throw new RuntimeException(String.format("Not found file %s",  e.getMessage()));
        } catch (IOException e) {
            errorHandler.createErrorFile(String.format("Input output error %s",  e.getMessage()));
            throw new RuntimeException(String.format("Input output error %s",  e.getMessage()));
        }
        return String.valueOf(builder);
    }

    @Override
    public Map<TypeRequest, String> parseStringToJson(String load) {
        Map<TypeRequest, String> response = new HashMap<>();
        Object obj;
        try {
            obj = new JSONParser().parse(load);
        } catch (ParseException e) {
            errorHandler.createErrorFile(String.format("Parse error %s",  e.getMessage()));
            throw new RuntimeException(String.format("Parse error %s",  e.getMessage()));
        }
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray array = (JSONArray) jsonObject.get("criterias");
        if (array != null) {
            StringBuilder builder = new StringBuilder("GG");
            for (int i = 0; i < array.size(); i++) {
                builder.append(array.get(i));
            }
            builder.append("GG");
            response.put(TypeRequest.CRITERIAS, builder.toString()
                    .replaceAll("\\}", ",")
                    .replaceAll("\\{", ",")
                    .replaceAll(",,", ",")
                    .replaceAll("GG,", "{")
                    .replaceAll(",GG", "}"));
            return response;
        } else {
            response.put(TypeRequest.STAT, load);
            return response;
        }
    }

    public Criterias parseCriterias(String jsonString) {
        Gson gson = new Gson();
        System.out.println(jsonString);
        Criterias criterias = gson.fromJson(jsonString, Criterias.class);
        Criterias model = new Criterias();
        if (criterias.equals(model)) {
            throw new CriteriaParseException("Empty value for search");
        }
        return criterias;
    }

    public Statistics parseStatistics(String jsonString) {
        Statistics statistics;
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                            (json, type, context) -> LocalDate.parse(json.getAsString()))
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                            (srs, typeOfSrs, context) -> new JsonPrimitive(srs.toString()))
                    .create();
            statistics = gson.fromJson(jsonString, Statistics.class);
            if (statistics.getStartDate() == null || statistics.getEndDate() == null) {
                errorHandler.createErrorFile("Empty value date for search");
                throw new StatisticParseException("Empty value date for search");
            }
        } catch (DateTimeParseException e) {
            errorHandler.createErrorFile(String.format("incorrect date format %s", e.getParsedString()));
            throw new StatisticParseException(String.format("incorrect date format %s", e.getParsedString()));
        }
        return statistics;
    }
}
