package com.paydaybank.dashboard.web.api;

import com.paydaybank.dashboard.dto.model.ProductDTO;
import com.paydaybank.dashboard.service.ProductService;
import com.paydaybank.dashboard.web.helper.ResponseHelper;
import com.paydaybank.dashboard.web.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity getAllProducts(){

        List<ProductDTO> products = productService.finAll();

        Response<List<ProductDTO>> response = ResponseHelper.ok(products);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity getProductById( @PathVariable Long productId ){

        Optional<ProductDTO> product = productService.findById(productId);

        Response<ProductDTO> response = ResponseHelper.ok(product.get());
        return ResponseEntity.ok()
                .location(URI.create("/products/"+productId))
                .body(response);
    }

    @PostMapping
    public ResponseEntity createNewProduct(@RequestBody ProductDTO productDTO ){

        Optional<ProductDTO> createdProduct = productService.create( Optional.of(productDTO) );

        Response<ProductDTO> response = ResponseHelper.ok(createdProduct.get());
        return ResponseEntity.created( URI.create("/products/"+ createdProduct.get().getId()))
                            .body(response);
    }

    @PutMapping
    public ResponseEntity updateProduct(@RequestBody ProductDTO productDTO ){

        Optional<ProductDTO> updatedProduct = productService.update( Optional.of(productDTO) );

        Response<ProductDTO> response = ResponseHelper.ok(updatedProduct.get());
        return ResponseEntity.ok()
                            .location( URI.create("/products/"+ updatedProduct.get().getId() ) )
                            .body(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity updateProduct(@PathVariable Long productId, @RequestParam("available") Boolean available ){

        Optional<ProductDTO> updatedProduct = productService.setAvailable( productId, available );

        Response<ProductDTO> response = ResponseHelper.ok(updatedProduct.get());
        return ResponseEntity.ok()
                .location( URI.create("/products/"+ updatedProduct.get().getId() ) )
                .body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity updateProduct(@PathVariable Long productId ){
        productService.deleteById( productId );
        return ResponseEntity.ok().body(ResponseHelper.ok());
    }
}
