package fr.inra.urgi.faidare.api.brapi.v1;

import fr.inra.urgi.faidare.api.BadRequestException;
import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.api.brapi.v1.exception.BrapiPaginationException;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiPagination;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiStatus;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Brapi exception handling intercepting every spring web Exception and generate a correct BrAPI response out of it.
 *
 * @author gcornut
 */
@ControllerAdvice(basePackages = "fr.inra.urgi.faidare.api.brapi.v1")
public class BrapiExceptionHandler {

    private static ResponseEntity<Object> createErrorResponse(
        HttpStatus httpStatus, List<BrapiStatus> statuses, BrapiPagination pagination
    ) {
        Object body;
        if (pagination != null) {
            body = ApiResponseFactory.createListResponse(pagination, statuses, null, false);
        } else {
            body = ApiResponseFactory.createSingleObjectResponse(null, statuses);
        }
        return ResponseEntity.status(httpStatus).body(body);
    }

    /**
     * Generate BrAPI error response from Exception
     *
     * Automatically extracts javax.validation error message into the BrAPI status
     */
    private static ResponseEntity<Object> createErrorResponse(
        Exception exception, HttpStatus httpStatus
    ) {
        List<BrapiStatus> statuses = new ArrayList<>();
        BrapiPagination pagination = null;

        if (exception instanceof BindException || exception instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult;
            if (exception instanceof BindException) {
                // Exception occurring when a user provided parameter that did not pass spring validation
                bindingResult = (BindException) exception;
            } else {
                // Exception occurring when a user provided request body parameter that did not pass spring validation
                bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            }

            List<ObjectError> errors = bindingResult.getAllErrors();
            errors.stream()
                // Convert to Exception
                .map(err -> new Exception(err.getDefaultMessage()))
                // Convert to BrapiStatus
                .map(ex -> ApiResponseFactory.createStatus(ex, httpStatus))
                .collect(Collectors.toCollection(() -> statuses));
        } else {
            // Other exceptions

            if (exception instanceof BrapiPaginationException) {
                // Brapi pagination exception when pagination validation failed (page index out of bound, negative page, etc.)
                pagination = ((BrapiPaginationException) exception).getPagination();
            }

            statuses.add(ApiResponseFactory.createStatus(exception, httpStatus));
        }

        return createErrorResponse(httpStatus, statuses, pagination);
    }

    /**
     * Generates Brapi not found response
     */
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(Exception exception) {
        return createErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    /**
     * Generates Brapi bad request response
     *
     * Handles {@link BindException} resulting from Spring's parsing of params
     *     (query params, path params, etc.)
     *
     * Handle {@link MethodArgumentNotValidException} resulting from Spring's validation of request body params
     *
     */
    @ExceptionHandler({
        BadRequestException.class,
        BindException.class,
        MethodArgumentNotValidException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<Object> handleBadRequest(Exception exception) {
        return createErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * Generates Brapi unexpected exception response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherException(Exception exception) {
        exception.printStackTrace();
        return createErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
