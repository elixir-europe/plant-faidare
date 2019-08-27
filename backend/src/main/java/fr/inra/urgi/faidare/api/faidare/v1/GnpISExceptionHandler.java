package fr.inra.urgi.faidare.api.faidare.v1;

import fr.inra.urgi.faidare.api.BadRequestException;
import fr.inra.urgi.faidare.api.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * GnpIS API exception handling
 *
 * @author gcornut
 */
@ControllerAdvice(basePackages = "fr.inra.urgi.faidare.api.faidare.v1")
public class GnpISExceptionHandler {

    /**
     * Return a simple HTTP response with a HTTP code corresponding to the error and the Exception message in the body
     */
    private static ResponseEntity<Object> createErrorResponse(
        Exception exception, HttpStatus httpStatus
    ) {
        String body = "HTTP Error " + httpStatus + ": " + exception.getLocalizedMessage();
        return ResponseEntity.status(httpStatus).body((Object) body);
    }

    /**
     * Generates Brapi not found response
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(Exception exception) {
        return createErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    /**
     * Generates Brapi bad request response
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
