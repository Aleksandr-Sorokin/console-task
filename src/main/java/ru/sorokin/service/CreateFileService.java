package ru.sorokin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sorokin.entity.Criterias;
import ru.sorokin.entity.Statistics;
import ru.sorokin.entity.json.*;
import ru.sorokin.model.dto.CustomerDto;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateFileService {
    private final ObjectMapper mapper;
    private final CriteriaService criteriaService;


    public String createFileCriteria(Criterias criterias){
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
            customerLastName = criteriaService
                    .findCustomerByLastName(criterias.getLastName());
        }
        if (criterias.getProductName() != null && criterias.getMinTimes() != null) {
            productToJson = new ProductToJson(criterias.getProductName(), criterias.getMinTimes());
            customerProduct = criteriaService
                    .findCustomerByProduct(criterias.getProductName(), criterias.getMinTimes());
        }
        if (criterias.getMinExpenses() != null && criterias.getMaxExpenses() != null) {
            expensesToJson = new ExpensesToJson(criterias.getMinExpenses(), criterias.getMaxExpenses());
            customerBetweenPrice = criteriaService
                    .findCustomerBetweenPrice(criterias.getMinExpenses(), criterias.getMaxExpenses());
        }
        if (criterias.getBadCustomers() != null) {
            badCustomerToJson = new BadCustomerToJson(criterias.getBadCustomers());
            customerPassive = criteriaService
                    .findPassiveCustomer(criterias.getBadCustomers());
        }
        String jsonCustomer = buildJsonForCriteria(gson.toJson(customerToJson), customerLastName);
        String jsonProduct = buildJsonForCriteria(gson.toJson(productToJson), customerProduct);
        String jsonExpenses = buildJsonForCriteria(gson.toJson(expensesToJson), customerBetweenPrice);
        String jsonPassive = buildJsonForCriteria(gson.toJson(badCustomerToJson), customerPassive);

        StringBuilder builder = new StringBuilder();
        builder.append("{\"type\": \"search\",\"results\":[");
        builder.append(jsonCustomer + "," +  jsonProduct + "," + jsonExpenses + "," + jsonPassive + "]}");

        try {
            Object json = mapper.readValue(builder.toString().replaceAll("\\\\", ""), Object.class);
            String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            System.out.println(result);
            return result;
        } catch (JsonProcessingException e) {
            log.error(String.valueOf(e.getStackTrace()));
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

    public String createFileStat(Statistics statistics) {
        ProductForStat productForStat;
        CustomerPurchases customerPurchases;
        TotalPurchases totalPurchases;

        return null;
    }
}
