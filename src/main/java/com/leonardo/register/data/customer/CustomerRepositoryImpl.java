package com.leonardo.register.data.customer;

import com.leonardo.register.core.customer.CustomerBO;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public CustomerBO findById(String id) {
        return CustomerBO.builder().build();
    }

    @Override
    public CustomerBO save(CustomerBO bo) {
        //TODO: Adicionar banco de dados que será escolhido
        return bo;
    }
}
