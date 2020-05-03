package com.paydaybank.dashboard.service;

import com.paydaybank.dashboard.dto.mapper.ProductMapper;
import com.paydaybank.dashboard.dto.model.ProductDTO;
import com.paydaybank.dashboard.exception.PaydayException;
import com.paydaybank.dashboard.model.Product;
import com.paydaybank.dashboard.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("ProductService should")
@SpringBootTest
public class ProductServiceTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("returns correctly existing data - findById")
    public void findByIdSuccessTest(){
        Product mockProduct = new Product(1L, "Test Product", 12.5, true, "Test product description");
        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1L);

        Optional<ProductDTO> responseProduct = productService.findById(1L);

        Assertions.assertTrue(responseProduct.isPresent(), "Data should return with id 1");

        ProductDTO responseProductDTO = responseProduct.get();
        Assertions.assertEquals(mockProduct.getName(), responseProductDTO.getName());
        Assertions.assertEquals(mockProduct.getDescription(), responseProductDTO.getDescription());
        Assertions.assertEquals(mockProduct.getAvailable(), responseProductDTO.getAvailable());
        Assertions.assertEquals(mockProduct.getPrice(), responseProductDTO.getPrice());
    }

    @Test
    @DisplayName("returns empty data for not existing data - findById")
    public void findByIdNotFoundTest(){
        doReturn(Optional.empty()).when(productRepository).findById(1L);
        Optional<ProductDTO> responseProduct = productService.findById(1L);
        Assertions.assertFalse(responseProduct.isPresent(), "Data should not return with id 1");
    }

    @Test
    @DisplayName("returns all of existing data - findAll")
    public void findAllSuccessTest(){
        Product prod1 = new Product(1L, "Test Product 1", 10.0, false, "Test product 1");
        Product prod2 = new Product(2L, "Test Product 2", 12.0, true, "Test product 2");
        doReturn(Arrays.asList(prod1,prod2)).when(productRepository).findAll();

        List<ProductDTO> responseProducts = productService.finAll();

        Assertions.assertEquals(2, responseProducts.size());
    }

    @Test
    @DisplayName("creates new product successfully")
    public void createProductSuccessfullyTest(){
        Product mockProduct = new Product(1L, "Test Product", 10.0, true, "Test product description");
        doReturn(mockProduct).when(productRepository).save(any());


        ProductDTO productToSave = ProductMapper.INSTANCE.productToProductDTO(mockProduct);
        Optional<ProductDTO> responseProduct = productService.create( Optional.of(productToSave) );

        Assertions.assertTrue(responseProduct.isPresent());

        ProductDTO savedProduct = responseProduct.get();
        Assertions.assertEquals(mockProduct.getName(), savedProduct.getName());
        Assertions.assertEquals(mockProduct.getDescription(), savedProduct.getDescription());
        Assertions.assertEquals(mockProduct.getAvailable(), savedProduct.getAvailable());
        Assertions.assertEquals(mockProduct.getPrice(), savedProduct.getPrice());
    }

    @Test
    @DisplayName("gets fail while updating product does not exists")
    public void updateNotExistingProductTest(){
        doReturn(Optional.empty()).when(productRepository).findById(1L);

        ProductDTO mockProduct = new ProductDTO(1L, "Test Product 1", 10.0, false, "Test product 1");
        Assertions.assertThrows(PaydayException.EntityNotFoundException.class, () -> {
            productService.update( Optional.of(mockProduct) );
        });
    }

    @Test
    @DisplayName("updates existing product successfully")
    public void updatesProductSuccessfullyTest(){
        Product mockProduct = new Product(1L, "Test Product", 10.0, false, "Test product");
        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1L);

        Product mockProductUpdated = new Product(1L, "Test Product Updated", 11.0, false, "Test product Updated");
        doReturn(mockProductUpdated).when(productRepository).save(any());

        ProductDTO mockProductUpdatedDTO = ProductMapper.INSTANCE.productToProductDTO(mockProductUpdated);
        Optional<ProductDTO> savedProductDTO = productService.update(Optional.of(mockProductUpdatedDTO));

        Assertions.assertTrue(savedProductDTO.isPresent());

        ProductDTO savedProduct = savedProductDTO.get();
        Assertions.assertEquals(mockProduct.getName(), savedProduct.getName());
        Assertions.assertEquals(mockProduct.getDescription(), savedProduct.getDescription());
        Assertions.assertEquals(mockProduct.getAvailable(), savedProduct.getAvailable());
        Assertions.assertEquals(mockProduct.getPrice(), savedProduct.getPrice());
    }

    @Test
    @DisplayName("gets fail while updating available property of the product does not exists")
    public void updateAvailableForNotExistingProductTest(){
        doReturn(Optional.empty()).when(productRepository).findById(1L);

        Assertions.assertThrows(PaydayException.EntityNotFoundException.class, () -> {
            productService.setAvailable( 1L, false);
        });
    }

    @Test
    @DisplayName("updates successfully available property of the existing product")
    public void updatesAvailableCorrectly(){
        Product mockProduct = new Product(1L, "Test Product", 10.0, false, "Test product");
        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1L);

        Product mockProductUpdated = new Product(1L, "Test Product", 10.0, true, "Test product");
        doReturn(mockProductUpdated).when(productRepository).save(any());

        ProductDTO mockProductUpdatedDTO = ProductMapper.INSTANCE.productToProductDTO(mockProductUpdated);
        Optional<ProductDTO> savedProductDTO = productService.update(Optional.of(mockProductUpdatedDTO));

        Assertions.assertTrue(savedProductDTO.isPresent());

        ProductDTO savedProduct = savedProductDTO.get();
        Assertions.assertEquals(mockProduct.getId(), savedProduct.getId());
        Assertions.assertEquals(mockProduct.getAvailable(), savedProduct.getAvailable());
    }

    @Test
    @DisplayName("gets fail while deleting the product does not exists by id")
    public void deletesNotExistingProductByIdTest(){
        doReturn(Optional.empty()).when(productRepository).findById(1L);

        Assertions.assertThrows(PaydayException.EntityNotFoundException.class, () -> {
            productService.deleteById( 1L);
        });

        verify(productRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("deletes successfully existing product by id")
    public void deletesExistingProductSuccessfullyById(){
        Product mockProduct = new Product(1L, "Test Product", 10.0, false, "Test product");
        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1L);

        boolean result = productService.deleteById( 1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(productRepository);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("gets fail while deleting the product does not exists by product")
    public void deletesNotExistingProductByProductTest(){
        doReturn(Optional.empty()).when(productRepository).findById(1L);

        ProductDTO mockProduct = new ProductDTO(1L, "Test Product 1", 10.0, false, "Test product 1");
        Assertions.assertThrows(PaydayException.EntityNotFoundException.class, () -> {
            productService.delete( Optional.of(mockProduct));
        });

        verify(productRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("deletes successfully existing product by product")
    public void deletesExistingProductSuccessfullyByProduct(){
        Product mockProduct = new Product(1L, "Test Product", 10.0, false, "Test product");
        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1L);

        ProductDTO mockProductDTO = ProductMapper.INSTANCE.productToProductDTO(mockProduct);
        boolean result = productService.delete( Optional.of(mockProductDTO) );

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(productRepository);

        Assertions.assertTrue(result);
    }
}
