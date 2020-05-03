package com.paydaybank.dashboard.service;

import com.paydaybank.dashboard.dto.model.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<ProductDTO> findById(Long id);

    List<ProductDTO> finAll();

    Optional<ProductDTO> create( Optional<ProductDTO> product );

    Optional<ProductDTO> update( Optional<ProductDTO> product );

    Optional<ProductDTO> setAvailable( Long id, Boolean available );

    Boolean deleteById( Long id );

    Boolean delete( Optional<ProductDTO> product );
}
