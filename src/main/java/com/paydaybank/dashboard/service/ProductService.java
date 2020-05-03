package com.paydaybank.dashboard.service;

import com.paydaybank.dashboard.dto.model.ProductDTO;
import java.util.Optional;

public interface ProductService extends BaseService<ProductDTO, Long> {

    Optional<ProductDTO> setAvailable( Long id, Boolean available );
}
