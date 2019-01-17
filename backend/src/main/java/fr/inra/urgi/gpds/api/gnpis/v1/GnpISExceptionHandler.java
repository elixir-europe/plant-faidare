package fr.inra.urgi.gpds.api.gnpis.v1;

import fr.inra.urgi.gpds.api.BadRequestException;
import fr.inra.urgi.gpds.api.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * GnpIS API exception handling
 *
 * @author gcornut
 *
 *
 */
@ControllerAdvice(basePackages = "fr.inra.urgi.gpds.api.gnpis.v1")
public class GnpISExceptionHandler extends ResponseEntityExceptionHandler {

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
