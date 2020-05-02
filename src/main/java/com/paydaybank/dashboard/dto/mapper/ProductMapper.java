package com.paydaybank.dashboard.dto.mapper;

import com.paydaybank.dashboard.dto.model.ProductDTO;
import com.paydaybank.dashboard.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    ProductDTO productToProductDTO(Product product);

    Product productDtoToProduct(ProductDTO product);

}
