package com.paydaybank.dashboard.web.helper;

import com.paydaybank.dashboard.web.model.Response;
import com.paydaybank.dashboard.web.enums.ResponseStatus;

public class ResponseHelper {
    
    public static <T> Response<T> badRequest() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.BAD_REQUEST);
        return response;
    }

    public static <T> Response<T> ok() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.OK);
        return response;
    }

    public static <T> Response<T> ok( T data ) {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.OK);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> unauthorized() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.UNAUTHORIZED);
        return response;
    }

    public static <T> Response<T> validationException() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.VALIDATION_EXCEPTION);
        return response;
    }

    public static <T> Response<T> wrongCredentials() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.WRONG_CREDENTIALS);
        return response;
    }

    public static <T> Response<T> accessDenied() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.ACCESS_DENIED);
        return response;
    }

    public static <T> Response<T> exception() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.EXCEPTION);
        return response;
    }

    public static <T> Response<T> notFound() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.NOT_FOUND);
        return response;
    }

    public static <T> Response<T> duplicateEntity() {
        Response<T> response = new Response<>();
        response.setStatus(ResponseStatus.DUPLICATE_ENTITY);
        return response;
    }
    
}
