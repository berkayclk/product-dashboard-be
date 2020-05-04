package com.paydaybank.dashboard.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paydaybank.dashboard.web.enums.ResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private ResponseStatus status;
    private T data;
}