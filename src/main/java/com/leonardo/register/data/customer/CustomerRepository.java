package com.leonardo.register.data.customer;

import com.leonardo.register.core.customer.CustomerBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepository {

    CustomerBO findById(String id);

    Page<CustomerBO> findByFilter(CustomerBO.Filter filter, Pageable pageable);

    CustomerBO save(CustomerBO bo);

}
