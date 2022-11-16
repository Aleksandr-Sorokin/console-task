package ru.sorokin.service;

import com.google.gson.Gson;
import ru.sorokin.entity.Criterias;
import ru.sorokin.entity.Statistics;
import ru.sorokin.model.dto.CustomerDto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateFileService {
    private CriteriaService criteriaService;

    public CreateFileService() {
        this.criteriaService = new CriteriaServiceImpl();
    }

    public String createFileCriteria(Criterias criterias) {
        Gson gson = new Gson();
        List<CustomerDto> customerLastName = new ArrayList<>();
        List<CustomerDto> customerProduct = new ArrayList<>();;
        List<CustomerDto> customerBetweenPrice = new ArrayList<>();;
        List<CustomerDto> customerPassive = new ArrayList<>();;
        if (criterias.getLastName() != null) {
            customerLastName = criteriaService
                    .findCustomerByLastName(criterias.getLastName());
        }
        if (criterias.getProductName() != null && criterias.getMinTimes() != null) {
            customerProduct = criteriaService
                    .findCustomerByProduct(criterias.getProductName(), criterias.getMinTimes());
        }
        if (criterias.getMinExpenses() != null && criterias.getMaxExpenses() != null) {
            customerBetweenPrice = criteriaService
                    .findCustomerBetweenPrice(criterias.getMinExpenses(), criterias.getMaxExpenses());
        }
        if (criterias.getBadCustomers() != null) {
            customerPassive = criteriaService
                    .findPassiveCustomer(criterias.getBadCustomers());
        }
        String jsonResponse = "";
        StringBuilder builderLastName = new StringBuilder();
        builderLastName.append("{\"criteria\":" + gson.toJson(criterias.getLastName()) + ",\"results\":");
        //List<CustomerDto> list = new ArrayList<>();
        /*list.add(new CustomerDto("Александр", "Сорокин"));
        list.add(new CustomerDto("Александр1", "Сорокин1"));
        list.add(new CustomerDto("Александр2", "Сорокин2"));*/
        String g = gson.toJson(customerLastName);
        builderLastName.append(g);
        builderLastName.append("}");
        System.out.println(builderLastName.toString());

        StringBuilder builderProduct = new StringBuilder();
        String gg = gson.toJson(criterias);
        System.out.println(" sdsd"  + gg);
        builderProduct.append("{\"criteria\":" + gg + ",\"results\":");
        String gsonProduct = gson.toJson(customerProduct);
        builderLastName.append(gsonProduct);
        builderLastName.append("}");
        System.out.println(builderProduct.toString());
        /*{
            "type": "search",
                "results": [
            {
                "criteria": {"lastName": "Иванов"},

                "results": [
                {"lastName": "Иванов", "firstName": "Антон"},
                {"lastName": "Иванов", "firstName": "Николай"}
                ]
            },
            {
                "criteria": {"productName": "Минеральная вода", "minTimes": 5},
                "results": [
                {"lastName": "Петров", "firstName": "Валентин"}

          ]
            }

  ]
        }*/

        /*StringBuilder builderAll = new StringBuilder();
        builderAll.append("\"type\"" + ":" + "\"search\"" + ",\"result\"" + ":");

        File file = new File("output.json");
        try {
            FileWriter writer = new FileWriter("output.json");
            writer.write(jsonResponse);
            writer.close();
        } catch (IOException e) {
            System.out.println("IOException WRITER !!!");
        }*/

        return builderLastName.toString();
    }

    public String createFileStat(Statistics statistics) {
        return null;
    }
}
