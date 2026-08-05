package com.leonardo.register.core.customer;

public interface CustomerService {

    CustomerBO findById(String id);

    CustomerBO create(CustomerBO bo);

}
