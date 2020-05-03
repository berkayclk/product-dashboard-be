package com.paydaybank.dashboard.service;

import com.paydaybank.dashboard.dto.mapper.ProductMapper;
import com.paydaybank.dashboard.dto.model.ProductDTO;
import com.paydaybank.dashboard.exception.EntityType;
import com.paydaybank.dashboard.exception.ExceptionType;
import com.paydaybank.dashboard.exception.PaydayException;
import com.paydaybank.dashboard.model.Product;
import com.paydaybank.dashboard.repository.ProductRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    ProductRepository productRepository;

    @Override
    public Optional<ProductDTO> findById(Long id) {
        Optional<Product> foundProduct = productRepository.findById(id);
        return foundProduct.map(ProductMapper.INSTANCE::productToProductDTO);
    }

    @Override
    public List<ProductDTO> finAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper.INSTANCE::productToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> create(Optional<ProductDTO> productDTO) {

        if( !productDTO.isPresent() ) {
            logger.warn("Attempted to create new product with empty parameter!");
            return Optional.empty();
        }

        Product product = productDTO.map(ProductMapper.INSTANCE::productDtoToProduct).get();
        logger.info("A new product creation process begun with {} product name and {} price.", product.getName(),  product.getPrice());

        Product savedProduct = productRepository.save(product);
        ProductDTO savedProductDTO = ProductMapper.INSTANCE.productToProductDTO(savedProduct);

        return Optional.ofNullable(savedProductDTO);
    }

    @Override
    public Optional<ProductDTO> update(Optional<ProductDTO> productDTO) {

        if( !productDTO.isPresent() || productDTO.get().getId() == null ) {
            logger.warn("Attempted to update product with missing identity info.");
            throw PaydayException.throwException(EntityType.PRODUCT, ExceptionType.MISSING_PARAMETER);
        }

        Optional<Product> product = productRepository.findById(productDTO.get().getId());
        if( !product.isPresent() ) {
            logger.warn("Attempted to update product that does not exists. Provided Id: {}", productDTO.get().getId());
            throw PaydayException.throwException(EntityType.PRODUCT, ExceptionType.NOT_FOUND);
        }

        Product foundProduct = product.get();
        Product updatedProduct = productDTO.map( p -> {
            foundProduct.setName(p.getName());
            foundProduct.setAvailable(p.getAvailable());
            foundProduct.setDescription(p.getDescription());
            foundProduct.setPrice(p.getPrice());
            return foundProduct;
        }).get();

        Product savedProduct = productRepository.save(updatedProduct);
        ProductDTO savedProductDTO = ProductMapper.INSTANCE.productToProductDTO(savedProduct);

        return Optional.ofNullable(savedProductDTO);
    }

    @Override
    public Optional<ProductDTO> setAvailable(Long id, Boolean available) {

        Optional<Product> foundProduct = productRepository.findById(id);
        if( !foundProduct.isPresent() ) {
            logger.warn("Attempted to change available properties of the product that does not exists");
            throw PaydayException.throwException(EntityType.PRODUCT, ExceptionType.NOT_FOUND);
        }

        Product product = foundProduct.get();
        if( product.getAvailable().equals(available) ) {
            return foundProduct.map(ProductMapper.INSTANCE::productToProductDTO);
        }

        logger.info("Changes available properties from {} to {} for the product has {} id.", product.getAvailable(),  available, product.getId());
        product.setAvailable(available);

        Product savedProduct = productRepository.save(product);
        ProductDTO savedProductDTO = ProductMapper.INSTANCE.productToProductDTO(savedProduct);

        return Optional.ofNullable(savedProductDTO);
    }

    @Override
    public Boolean deleteById(Long id) {

        Optional<Product> foundProduct = productRepository.findById(id);
        if( !foundProduct.isPresent() ) {
            logger.warn("Attempted to delete the product that does not exists");
            throw PaydayException.throwException(EntityType.PRODUCT, ExceptionType.NOT_FOUND);
        }

        productRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean delete(Optional<ProductDTO> productDTO) {

        if( !productDTO.isPresent() || productDTO.get().getId() == null ) {
            logger.warn("Attempted to delete product with missing identity info.");
            throw PaydayException.throwException(EntityType.PRODUCT, ExceptionType.MISSING_PARAMETER);
        }

        Optional<Product> product = productRepository.findById(productDTO.get().getId());
        if( !product.isPresent() ) {
            logger.warn("Attempted to delete product that does not exists. Provided Id: {}", productDTO.get().getId());
            throw PaydayException.throwException(EntityType.PRODUCT, ExceptionType.NOT_FOUND);
        }

        productRepository.deleteById(product.get().getId());

        return true;
    }



}
