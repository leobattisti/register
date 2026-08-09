package com.leonardo.register.core.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerBO findById(String id);

    Page<CustomerBO> findByFilter(CustomerBO.Filter filter, Pageable pageable);

    CustomerBO create(CustomerBO bo);

}
