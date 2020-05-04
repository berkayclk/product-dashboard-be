package com.paydaybank.dashboard.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank(message = "name is required")
    @Length(max = 50, min = 2, message = "Length of the name field should be between 2 and 50")
    private String name;

    @NotBlank(message = "price is required")
    @Min(value = 0, message = "price should be greater than 0")
    private Double price;

    private Boolean available = true;

    @Length(max = 1000,message = "length of the description should be lower than 1000.")
    private String description;
}
