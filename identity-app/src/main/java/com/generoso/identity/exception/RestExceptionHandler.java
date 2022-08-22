package com.generoso.identity.exception;

import com.generoso.identity.exception.error.ErrorDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body,
                                                             HttpHeaders headers, HttpStatus status,
                                                             WebRequest request) {
        //@formatter:off
        var errorDetail = ErrorDetail.builder()
                .localDateTime(LocalDateTime.now())
                .status(status.value())
                .title("Internal Exception")
                .detail(exception.getMessage())
                .build();
        //@formatter:on
        return new ResponseEntity<>(errorDetail, headers, status);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ErrorDetail> handleRequestException(RequestException requestException) {
        ;
        //@formatter:off
        var errorDetail = ErrorDetail.builder()
                .localDateTime(LocalDateTime.now())
                .status(requestException.getStatusCode())
                .title("Request exception")
                .detail(requestException.getMessage())
                .build();
        //@formatter:on
        return new ResponseEntity<>(errorDetail, HttpStatus.valueOf(requestException.getStatusCode()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetail> handleResourceNotFoundException(ResourceNotFoundException rfnException) {
        var status = HttpStatus.NOT_FOUND;
        //@formatter:off
        var details = ErrorDetail.builder()
                .localDateTime(LocalDateTime.now())
                .status(status.value())
                .title("Resource Not Found")
                .detail(rfnException.getMessage())
                .build();
        //@formatter:on
        return new ResponseEntity<>(details, status);
    }
}
