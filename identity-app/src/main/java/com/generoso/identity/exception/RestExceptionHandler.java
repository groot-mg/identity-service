package com.generoso.identity.exception;

import com.generoso.identity.exception.error.ErrorDetail;
import com.generoso.identity.exception.error.ValidationErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.NotAuthorizedException;
import java.util.stream.Collectors;

import static java.lang.String.format;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body,
                                                             HttpHeaders headers, HttpStatus status,
                                                             WebRequest request) {
        var errorDetail = ErrorDetail.builder()
                .status(status.value())
                .error("Internal Exception")
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetail, headers, status);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        var fieldErrors = exception.getBindingResult().getFieldErrors();
        var fields = fieldErrors.stream().map(FieldError::getField)
                .collect(Collectors.joining(", "));
        var fieldMessages = fieldErrors.stream()
                .map(fieldError -> format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        var errorDetails = ValidationErrorDetails.builder()
                .status(status.value())
                .error("Field validation error")
                .field(fields)
                .fieldMessage(fieldMessages)
                .build();
        return new ResponseEntity<>(errorDetails, headers, status);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorDetail> handleNotAuthorizedException(NotAuthorizedException exception) {
        var status = HttpStatus.UNAUTHORIZED;
        var errorDetails = ErrorDetail.builder()
                .status(status.value())
                .error("Unauthorized")
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, status);
    }


    // review if these exceptions are useful from here to bottom
//    @ExceptionHandler(RequestException.class)
//    public ResponseEntity<ErrorDetail> handleRequestException(RequestException requestException) {
//        var status = HttpStatus.valueOf(requestException.getStatusCode());
//        var errorDetail = ErrorDetail.builder()
//                .status(status.value())
//                .error("Request exception")
//                .detail(requestException.getMessage())
//                .build();
//        return new ResponseEntity<>(errorDetail, status);
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorDetail> handleResourceNotFoundException(ResourceNotFoundException exception) {
//        var status = HttpStatus.NOT_FOUND;
//        var errorDetails = ErrorDetail.builder()
//                .status(status.value())
//                .error("Resource Not Found")
//                .detail(exception.getMessage())
//                .build();
//        return new ResponseEntity<>(errorDetails, status);
//    }
}
