package ru.sorokin.service;

import org.springframework.stereotype.Service;
import ru.sorokin.model.dto.CustomerDto;

import java.util.List;
@Service
public class CriteriaServiceImpl implements CriteriaService{

    @Override
    public List<CustomerDto> findCustomerByLastName(String lastName) {
        return null;
    }

    @Override
    public List<CustomerDto> findCustomerByProduct(String product, Integer limit) {
        return null;
    }

    @Override
    public List<CustomerDto> findCustomerBetweenPrice(Integer min, Integer max) {
        return null;
    }

    @Override
    public List<CustomerDto> findPassiveCustomer(Integer limit) {
        return null;
    }
}
