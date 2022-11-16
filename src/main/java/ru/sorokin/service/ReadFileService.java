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
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("IOException!!!");
        }
        return String.valueOf(builder);
    }

    @Override
    public Map<TypeRequest, String> parseStringToJson(String load) {
        Map<TypeRequest, String> response = new HashMap<>();
        Object obj = null;
        try {
            obj = new JSONParser().parse(load);
        } catch (ParseException e) {
            throw new RuntimeException(e);
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
        return gson.fromJson(jsonString, Criterias.class);
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
        } catch (DateTimeParseException e) {
            errorHandler.createErrorFile(String.format("incorrect date format %s", e.getParsedString()));
            throw new StatisticParseException(String.format("incorrect date format %s", e.getParsedString()));
        }
        return statistics;
    }
}
