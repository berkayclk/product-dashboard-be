package com.paydaybank.dashboard.web.exception;

import com.paydaybank.dashboard.exception.PaydayException;
import com.paydaybank.dashboard.web.helper.ResponseHelper;
import com.paydaybank.dashboard.web.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(CustomizedExceptionHandler.class);

    @ExceptionHandler(PaydayException.EntityNotFoundException.class)
    public final ResponseEntity handleNotFountExceptions(Exception ex, WebRequest request) {
        Response notFoundResponse = ResponseHelper.notFound();
        return new ResponseEntity(notFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaydayException.DuplicateEntityException.class)
    public final ResponseEntity handleConflictException(Exception ex, WebRequest request) {
        Response duplicateResponse = ResponseHelper.duplicateEntity();
        return new ResponseEntity(duplicateResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity handleDataException(Exception ex, WebRequest request) {
        logger.error("A data exception occurred! Message: {}", ex.getMessage());

        Response runtimeErrorResponse = ResponseHelper.validationException();
        return new ResponseEntity(runtimeErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity handleGenericException(Exception ex, WebRequest request) {
        logger.error("A runtime exception occurred! Message: {}", ex.getMessage());

        Response runtimeErrorResponse = ResponseHelper.exception();
        return new ResponseEntity(runtimeErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}