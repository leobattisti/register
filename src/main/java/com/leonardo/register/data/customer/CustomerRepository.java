package com.leonardo.register.data.customer;

import com.leonardo.register.core.customer.CustomerBO;

public interface CustomerRepository {

    CustomerBO findById(String id);

    CustomerBO save(CustomerBO bo);

}
