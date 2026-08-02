package com.leonardo.register.api.customer;

import com.leonardo.register.core.customer.CustomerBO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerApiConverter {

    CustomerBO toBusiness(CustomerDTO dto);

    CustomerDTO toResponse(CustomerBO bo);

}
