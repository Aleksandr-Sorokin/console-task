package ru.sorokin.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.sorokin.config.ModelMapperUtil;
import ru.sorokin.enums.TypeResponse;
import ru.sorokin.exceptions.controller.ErrorHandler;
import ru.sorokin.exceptions.model.StatisticParseException;
import ru.sorokin.model.*;
import ru.sorokin.model.dto.CustomerDto;
import ru.sorokin.model.entity.Statistics;
import ru.sorokin.model.entity.time.VolumeWorkDayAndWeekEnd;
import ru.sorokin.storage.CustomerStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final ModelMapper mapper;
    private final CustomerStorage customerStorage;
    private final ServiceTime serviceTime;
    private final ErrorHandler errorHandler;

    @Override
    public List<CustomerDto> findCustomerByLastName(String lastName) {
        if (lastName != null) {
            List<Customer> customers = customerStorage.findCustomerByLastName(lastName);
            return ModelMapperUtil.convertList(customers, this::convertToDto);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDto> findCustomerByProduct(String product, Integer limit) {
        if (product != null && limit != null) {
            List<Customer> customers = customerStorage.findCustomerByProduct(product, limit);
            return ModelMapperUtil.convertList(customers, this::convertToDto);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDto> findCustomerBetweenPrice(Integer min, Integer max) {
        if (min != null && max != null) {
            List<Customer> customers = customerStorage.findCustomerBetweenPrice(min, max);
            return ModelMapperUtil.convertList(customers, this::convertToDto);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDto> findPassiveCustomer(Integer limit) {
        if (limit != null) {
            List<Customer> customers = customerStorage.findPassiveCustomer(limit);
            return ModelMapperUtil.convertList(customers, this::convertToDto);
        }
        return new ArrayList<>();
    }

    @Override
    public StatisticPurchases findAllStatBetweenDate(Statistics statistics) {
        if (statistics != null || statistics.getStartDate() != null || statistics.getEndDate() != null) {
            VolumeWorkDayAndWeekEnd worksDayAndWeekEnd = serviceTime
                    .getWeekEndDay(statistics.getStartDate(), statistics.getEndDate());
            List<LocalDate> dates = worksDayAndWeekEnd.getWeekEnd();
            List<StatisticBetweenDate> statisticsBetweenDate = customerStorage.findAllStatBetweenDate(dates);
            List<CustomerPurchases> customersWithPurchases = new ArrayList<>();
            Double totalExpenses = 0d;
            StatisticPurchases statisticAllPurchases = new StatisticPurchases();
            statisticAllPurchases.setType(TypeResponse.STAT);
            statisticAllPurchases.setTotalDays(worksDayAndWeekEnd.getWorkDay());
            Map<Long, List<StatisticBetweenDate>> purchasesByCustomerGroups = new HashMap<>();
            Set<Long> keys = new HashSet<>();

            // Группирует по покупателям списки покупок в HashMap и список ключей для быстрого доступа
            for (int i = 0; i < statisticsBetweenDate.size(); i++) {
                StatisticBetweenDate statistic = statisticsBetweenDate.get(i);
                Long key = statistic.getCustomer().getId();
                keys.add(key);
                List<StatisticBetweenDate> list = purchasesByCustomerGroups.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(statistic);
                purchasesByCustomerGroups.put(key, list);
            }
            List<Long> idCustomersKeys = keys.stream().collect(Collectors.toList());
            for (int i = 0; i < idCustomersKeys.size(); i++) {
                List<StatisticBetweenDate> statisticWithOneCustomer = purchasesByCustomerGroups.get(idCustomersKeys.get(i));
                String nameCustomer = new StringBuilder(statisticWithOneCustomer.get(0).getCustomer().getLastName() + " " +
                        statisticWithOneCustomer.get(0).getCustomer().getFirstName()).toString();
                Double totalExpensesIndividual = 0d;
                List<Purchases> purchasesIndividual = new ArrayList<>();
                for (int j = 0; j < statisticWithOneCustomer.size(); j++) {
                    Purchases purchase = new Purchases(
                            statisticWithOneCustomer.get(j).getProduct().getTitle(),
                            statisticWithOneCustomer.get(j).getSum());
                    purchasesIndividual.add(purchase);
                    totalExpensesIndividual += statisticWithOneCustomer.get(j).getSum();
                }
                Comparator<Purchases> comparePurchases = Comparator.comparing(Purchases::getExpenses).reversed();
                Collections.sort(purchasesIndividual, comparePurchases);
                CustomerPurchases customerPurchases = new CustomerPurchases(
                        nameCustomer, purchasesIndividual, totalExpensesIndividual);
                customersWithPurchases.add(customerPurchases);
                totalExpenses += totalExpensesIndividual;
            }
            Comparator<CustomerPurchases> totalCompare = Comparator
                    .comparing(CustomerPurchases::getTotalExpenses).reversed();
            Collections.sort(customersWithPurchases, totalCompare);
            statisticAllPurchases.setCustomers(customersWithPurchases);
            statisticAllPurchases.setTotalExpenses(totalExpenses);
            statisticAllPurchases.setAvgExpenses(totalExpenses / idCustomersKeys.size());
            return statisticAllPurchases;
        } else {
            errorHandler.createErrorFile(String.format("Date error start = %s, end = %s",
                    statistics.getStartDate(), statistics.getEndDate()));
            throw new StatisticParseException(String.format("Проблема с датой %s", statistics));
        }
    }

    private CustomerDto convertToDto(Customer customer) {
        return mapper.map(customer, CustomerDto.class);
    }
}
