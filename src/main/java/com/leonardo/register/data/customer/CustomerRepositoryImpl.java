package com.leonardo.register.data.customer;

import com.leonardo.register.core.customer.CustomerBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    //TODO: Adicionar banco de dados que será escolhido
    @Override
    public CustomerBO findById(String id) {
        return CustomerBO.builder().build();
    }

    @Override
    public Page<CustomerBO> findByFilter(CustomerBO.Filter filter, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public CustomerBO save(CustomerBO bo) {
        return bo;
    }
}
