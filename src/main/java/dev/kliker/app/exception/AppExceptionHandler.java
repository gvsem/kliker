package dev.kliker.app.exception;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class AppExceptionHandler // extends ResponseEntityExceptionHandler
{

    @Value("classpath:static/404.html")
    Resource html404;

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<Object> handlePayloadLarge(final Exception ex, final WebRequest request) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(final Exception ex, final WebRequest request) {
        return ResponseEntity.status(NOT_FOUND).body(null);
    }



}