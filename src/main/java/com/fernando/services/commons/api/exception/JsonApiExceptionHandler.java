package com.fernando.services.commons.api.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fernando.services.commons.api.model.ApiError;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * JSON:API compliant exception handler. Refer to
 * https://jsonapi.org/examples/#error-objects-source-usage
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class JsonApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String STATUS = "status";

    @Autowired
    private Environment env;

    private String activeProfile;
    
    private static Boolean isDev = null;

    /**
	 * General handler for unknown exceptions
     */
     // @ExceptionHandler({Exception.class, TypeMismatchException.class})
    @ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);

        HttpStatus status = responseStatus != null ? responseStatus.code() : HttpStatus.INTERNAL_SERVER_ERROR;
        

        return buildResponseEntity(exception, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
			TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ex, status, request);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return buildResponseEntity(ex, status, request);
	}


    @ExceptionHandler({EntityNotFoundException.class})
	public ResponseEntity<Object> handleEntityNotFound(Exception exception, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return buildResponseEntity(exception, status, request, HttpStatus.NOT_FOUND.value(), "Entity not found" );
    }

    public ResponseEntity<Object> buildResponseEntity(final Exception exception, final HttpStatus status, final WebRequest request, Integer code, String title) {
        return buildResponseEntity(exception, status, request, code, title, null);
    }

    public ResponseEntity<Object> buildResponseEntity(final Exception exception, final HttpStatus status, final WebRequest request) {
        return buildResponseEntity(exception, status, request, null, null, null);
    }

    public ResponseEntity<Object> buildResponseEntity(final Exception exception, final HttpStatus status, final WebRequest request, 
    Integer code, String title, final List<String> subErrors) {

        ApiError.ApiErrorBuilder errorBuilder = ApiError.builder();
        errorBuilder
            .status(status.value())
            .code(code)
            .title(title + " - " + activeProfile)
            .type(exception.getClass().getSimpleName())
            .detail(exception.getLocalizedMessage())
            // .trace(getStackTrace(exception))
            .path(request.getDescription(false))
            .subErrors(subErrors);

        if (isDev()) {
            errorBuilder.trace(getStackTrace(exception));
            exception.printStackTrace();
        }

        final Map<String, Object> body = new LinkedHashMap<>();
        body.put(STATUS, status.value());
        body.put("error", "CUSTOM: " + exception.getMessage());
        body.put("debugMessage", "CUSTOM: " + exception.getMessage());

        // final String path = request.getDescription(false);
        // body.put(STATUS, status.value());
        // body.put(ERRORS, errors);
        // body.put(TYPE, exception.getClass().getSimpleName());
        // body.put(PATH, path);
        // body.put(MESSAGE, getMessageForStatus(status));
        // final String errorsMessage = CollectionUtils.isNotEmpty(errors)
        //         ? errors.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(LIST_JOIN_DELIMITER))
        //         : status.getReasonPhrase();
        // local_logger.error(ERRORS_FOR_PATH, errorsMessage, path);
        return new ResponseEntity<>(errorBuilder.build(), status);
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    private Boolean isDev() {
        if (JsonApiExceptionHandler.isDev == null) {
            JsonApiExceptionHandler.setIsDev(Arrays.asList(env.getActiveProfiles()).contains("dev"));
            activeProfile = String.join(",", env.getActiveProfiles());
        }
        return JsonApiExceptionHandler.isDev;
    }


    private static void setIsDev(boolean isDev) {
        JsonApiExceptionHandler.isDev = isDev;
    }
}
