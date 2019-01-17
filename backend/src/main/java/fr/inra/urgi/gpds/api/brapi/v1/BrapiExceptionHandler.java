package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.api.BadRequestException;
import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.api.brapi.v1.exception.BrapiPaginationException;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiPagination;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Brapi exception handling
 *
 * @author gcornut
 *
 *
 */
@ControllerAdvice(basePackages = "fr.inra.urgi.gpds.api.brapi.v1")
public class BrapiExceptionHandler extends ResponseEntityExceptionHandler {

	private static ResponseEntity<Object> createErrorResponse(
        HttpStatus httpStatus, List<BrapiStatus> statuses, BrapiPagination pagination
	) {
		Object body = BrapiResponseFactory.createListResponse(pagination, statuses, null, false);
		return ResponseEntity.status(httpStatus).body(body);
	}

	private static ResponseEntity<Object> createErrorResponse(
			Exception exception, HttpStatus httpStatus
	) {
		List<BrapiStatus> statuses = new ArrayList<>();
		BrapiPagination pagination = null;

		if (exception instanceof BindException) {
			// Exception occurring when a user provided parameter that did not pass spring validation

			BindException bindException = (BindException) exception;
			List<FieldError> fieldErrors = bindException.getFieldErrors();
			statusFromFieldError(httpStatus, statuses, fieldErrors);
		} else if (exception instanceof MethodArgumentNotValidException) {
			// Exception occurring when a user provided request body parameter that did not pass spring validation

			MethodArgumentNotValidException notValidException = (MethodArgumentNotValidException) exception;

			ObjectError globalError = notValidException.getBindingResult().getGlobalError();
			if (globalError != null) {
				Exception e = new Exception(globalError.getDefaultMessage());
				statuses.add(BrapiResponseFactory.createStatus(e, httpStatus));
			}
			List<FieldError> fieldErrors = notValidException.getBindingResult().getFieldErrors();
			statusFromFieldError(httpStatus, statuses, fieldErrors);
		} else {
			// Other exceptions

			if (exception instanceof BrapiPaginationException) {
				// Brapi pagination exception when pagination validation failed (page index out of bound, negative page, etc.)
				pagination = ((BrapiPaginationException) exception).getPagination();
			}

			statuses.add(BrapiResponseFactory.createStatus(exception, httpStatus));
		}

		return createErrorResponse(httpStatus, statuses, pagination);
	}

	/**
	 * Convert spring field errors into BrAPI statuses
	 */
	private static void statusFromFieldError(HttpStatus httpStatus, List<BrapiStatus> statuses, List<FieldError> fieldErrors) {
		if (fieldErrors != null) {
			for (FieldError error : fieldErrors) {
				Exception e = new Exception(error.getDefaultMessage());
				statuses.add(BrapiResponseFactory.createStatus(e, httpStatus));
			}
		}
	}

	/**
	 * Handle {@link BindException} resulting from Spring's parsing of params
	 * (query params, path params, etc.)
	 */
	@Override
	protected ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return handleBadRequest(exception);
	}

	/**
	 * Handle exception resulting from Spring's validation of request body params
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleBadRequest(exception);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleOtherException(ex);
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
	@ExceptionHandler(BadRequestException.class)
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
