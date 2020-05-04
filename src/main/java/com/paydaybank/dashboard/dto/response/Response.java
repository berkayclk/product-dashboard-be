package com.paydaybank.dashboard.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paydaybank.dashboard.web.enums.ResponseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "GenericResponse", description = "Generic Response Object")
public class Response<T> {
    @ApiModelProperty(value = "Application Specific Status Enum", required = true)
    private ResponseStatus status;

    @ApiModelProperty(value = "Data", required = false)
    private T data;

    @ApiModelProperty(value = "errors", required = false)
    private Object errors;
}