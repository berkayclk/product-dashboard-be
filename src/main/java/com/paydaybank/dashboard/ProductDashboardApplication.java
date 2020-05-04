package com.paydaybank.dashboard;

import com.paydaybank.dashboard.dto.model.ProductDTO;
import com.paydaybank.dashboard.dto.model.UserDTO;
import com.paydaybank.dashboard.enums.UserRoles;
import com.paydaybank.dashboard.service.ProductService;
import com.paydaybank.dashboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class ProductDashboardApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProductDashboardApplication.class, args);
    }

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        UserDTO user = new UserDTO("admin", "admin@admin.net", "admin","ADMIN" );
        user.addRole(UserRoles.ADMIN);
        UserDTO user2 = new UserDTO("user", "user@user.netuser@user.net", "user","USER" );
        ProductDTO prod1 = new ProductDTO(null, "Product 1", 12.5, true, "Test product 1");
        ProductDTO prod2 = new ProductDTO(null, "Product 2", 2.5, true, "Test product 2");
        ProductDTO prod3 = new ProductDTO(null, "Product 3", 22.5, false, "Test product 3");

        List<UserDTO> createdUsers = Stream.of(user, user2)
                                            .map(Optional::of)
                                            .map(userService::create)
                                            .filter(Optional::isPresent)
                                            .map(Optional::get)
                                            .collect(Collectors.toList());

        List<ProductDTO> createdProducts = Stream.of(prod1, prod2, prod3)
                .map(Optional::of)
                .map(productService::create)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        System.out.println("******* Created User *******\r\n" + createdUsers.stream().map(u->u.toString() + "\r\n").collect(Collectors.joining()));
        System.out.println("******* Created Products *******\r\n" + createdProducts.stream().map(u->u.toString() + "\r\n").collect(Collectors.joining()) );
    }
}
