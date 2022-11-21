package ru.sorokin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.sorokin.enums.TypeResponse;
import ru.sorokin.exceptions.controller.ErrorHandler;
import ru.sorokin.model.StatisticPurchases;
import ru.sorokin.model.dto.CustomerDto;
import ru.sorokin.model.dto.StatisticPurchasesDto;
import ru.sorokin.model.entity.Criterias;
import ru.sorokin.model.entity.Statistics;
import ru.sorokin.model.entity.json.BadCustomerToJson;
import ru.sorokin.model.entity.json.CustomerToJson;
import ru.sorokin.model.entity.json.ExpensesToJson;
import ru.sorokin.model.entity.json.ProductToJson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateFileService implements CreateService {
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final ErrorHandler errorHandler;

    @Override
    public String createFileCriteria(Criterias criterias, File fileOutput) {
        Gson gson = new Gson();
        List<CustomerDto> customerLastName = new ArrayList<>();
        List<CustomerDto> customerProduct = new ArrayList<>();
        List<CustomerDto> customerBetweenPrice = new ArrayList<>();
        List<CustomerDto> customerPassive = new ArrayList<>();
        CustomerToJson customerToJson = null;
        ProductToJson productToJson = null;
        ExpensesToJson expensesToJson = null;
        BadCustomerToJson badCustomerToJson = null;
        if (criterias.getLastName() != null) {
            customerToJson = new CustomerToJson(criterias.getLastName());
            customerLastName = customerService
                    .findCustomerByLastName(criterias.getLastName());
        }
        if (criterias.getProductName() != null && criterias.getMinTimes() != null) {
            productToJson = new ProductToJson(criterias.getProductName(), criterias.getMinTimes());
            customerProduct = customerService
                    .findCustomerByProduct(criterias.getProductName(), criterias.getMinTimes());
        }
        if (criterias.getMinExpenses() != null && criterias.getMaxExpenses() != null) {
            expensesToJson = new ExpensesToJson(criterias.getMinExpenses(), criterias.getMaxExpenses());
            customerBetweenPrice = customerService
                    .findCustomerBetweenPrice(criterias.getMinExpenses(), criterias.getMaxExpenses());
        }
        if (criterias.getBadCustomers() != null) {
            badCustomerToJson = new BadCustomerToJson(criterias.getBadCustomers());
            customerPassive = customerService
                    .findPassiveCustomer(criterias.getBadCustomers());
        }
        String jsonCustomer = buildJsonForCriteria(gson.toJson(customerToJson), customerLastName);
        String jsonProduct = buildJsonForCriteria(gson.toJson(productToJson), customerProduct);
        String jsonExpenses = buildJsonForCriteria(gson.toJson(expensesToJson), customerBetweenPrice);
        String jsonPassive = buildJsonForCriteria(gson.toJson(badCustomerToJson), customerPassive);

        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\":\"" + TypeResponse.SEARCH + "\",\"results\":[");
        builder.append(jsonCustomer + "," + jsonProduct + "," + jsonExpenses + "," + jsonPassive + "]}");

        try {
            Object json = objectMapper.readValue(builder.toString().replaceAll("\\\\", ""), Object.class);
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            createFileOutput(result, fileOutput);
            return result;
        } catch (JsonProcessingException e) {
            errorHandler.createErrorFile(String.valueOf(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    private String buildJsonForCriteria(String criteria, List<CustomerDto> resultFromDB) {
        Gson gson = new Gson();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"criteria\":" + criteria + ",\"results\":");
        String result = gson.toJson(resultFromDB);
        if (result == null || result.equals("null")) {
            builder.append("\"По Вашему запросу ничего не найдено\"");
        } else {
            builder.append(result);
        }
        builder.append("}");
        return String.valueOf(builder);
    }

    @Override
    public String createFileStat(Statistics statistics, File fileOutput) {
        StatisticPurchases statisticPurchases = customerService.findAllStatBetweenDate(statistics);
        StatisticPurchasesDto statisticPurchasesDto = modelMapper.map(statisticPurchases, StatisticPurchasesDto.class);
        try {
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(statisticPurchasesDto);
            createFileOutput(result, fileOutput);
        } catch (JsonProcessingException e) {
            errorHandler.createErrorFile(e.getStackTrace().toString());
            throw new RuntimeException(e);
        }
        return null;
    }

    private void createFileOutput(String jsonString, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            errorHandler.createErrorFile("IOException данные не записались");
        }
    }
}
