package com.paydaybank.dashboard.web.api;

import com.paydaybank.dashboard.dto.model.ProductDTO;
import com.paydaybank.dashboard.service.ProductService;
import com.paydaybank.dashboard.web.helper.ResponseHelper;
import com.paydaybank.dashboard.web.model.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Api(value = "Product Api", tags = {"Product"}, produces = "appliation/json", consumes = "application/json")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    @ApiOperation(
            value = "Get All Products",
            notes = "This endpoint returns all products. You must provide an auth token."
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class )
    })
    public ResponseEntity<Response<List<ProductDTO>>> getAllProducts(){

        List<ProductDTO> products = productService.finAll();

        Response<List<ProductDTO>> response = ResponseHelper.ok(products);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{productId}")
    @ApiOperation(
            value = "Get One Product By Id",
            notes = "This endpoint returns a product by id. You must provide an auth token."
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no data matches information you gave!", response = Response.class )
    })
    public ResponseEntity<Response<ProductDTO>> getProductById( @PathVariable Long productId ){

        Optional<ProductDTO> product = productService.findById(productId);

        Response<ProductDTO> response = ResponseHelper.ok(product.orElse(null));
        return ResponseEntity.ok()
                .location(URI.create("/products/"+productId))
                .body(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(
            value = "Creates New Product [ADMIN ONLY]",
            notes = "This endpoint creates a new product. You must provide an auth token of the admin user.",
            code = 201
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 403, message = "Forbidden! You must provide a jwt token of an ADMIN USER!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class )
    })
    public ResponseEntity<Response<ProductDTO>> createNewProduct(@RequestBody ProductDTO productDTO ){

        Optional<ProductDTO> createdProduct = productService.create( Optional.of(productDTO) );

        Response<ProductDTO> response = ResponseHelper.ok(createdProduct.orElse(null));
        return ResponseEntity.created( URI.create(createdProduct.map(p -> "/products/"+ p.getId()).orElse("")))
                            .body(response);
    }

    @PutMapping
    @ApiOperation(
            value = "Update Product",
            notes = "This endpoint updates product. You must provide an auth token. Given object must contains id field!"
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no data matches information you gave!", response = Response.class )
    })
    public ResponseEntity<Response<ProductDTO>> updateProduct(@RequestBody ProductDTO productDTO ){

        Optional<ProductDTO> updatedProduct = productService.update( Optional.of(productDTO) );

        Response<ProductDTO> response = ResponseHelper.ok(updatedProduct.orElse(null));
        return ResponseEntity.ok()
                            .location( URI.create(updatedProduct.map(p -> "/products/"+ p.getId()).orElse("")) )
                            .body(response);
    }

    @PutMapping("/{productId}")
    @ApiOperation(
            value = "Set Available Property Of Product",
            notes = "This endpoint updates available property of the product. You must provide an auth token."
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no data matches information you gave!", response = Response.class )
    })
    public ResponseEntity<Response<ProductDTO>> updateProduct(@PathVariable Long productId, @RequestParam("available") Boolean available ){

        Optional<ProductDTO> updatedProduct = productService.setAvailable( productId, available );

        Response<ProductDTO> response = ResponseHelper.ok(updatedProduct.orElse(null));
        return ResponseEntity.ok()
                .location( URI.create(updatedProduct.map(p -> "/products/"+ p.getId()).orElse("") ) )
                .body(response);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(
            value = "Deletes Product by Id [ADMIN ONLY]",
            notes = "This endpoint deletes product by id. You must provide an auth token of the admin user."
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class),
            @ApiResponse(code = 403, message = "Forbidden! You must provide a jwt token of an ADMIN USER!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no data matches information you gave!", response = Response.class )
    })
    public ResponseEntity updateProduct(@PathVariable Long productId ){
        productService.deleteById( productId );
        return ResponseEntity.ok().body(ResponseHelper.ok());
    }
}
