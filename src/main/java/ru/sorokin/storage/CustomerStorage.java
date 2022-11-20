package ru.sorokin.storage;

import ru.sorokin.model.Customer;
import ru.sorokin.model.StatisticBetweenDate;

import java.time.LocalDate;
import java.util.List;

public interface CustomerStorage {
    List<Customer> findCustomerByLastName(String lastName);

    List<Customer> findCustomerByProduct(String product, Integer limit);

    List<Customer> findCustomerBetweenPrice(Integer min, Integer max);

    List<Customer> findPassiveCustomer(Integer limit);

    List<StatisticBetweenDate> findAllStatBetweenDate(List<LocalDate> dates);
}
