package com.saw.sns.exception;

import com.saw.sns.common.SnsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({ValidationErrorException.class, OperationFailedException.class})
    public ResponseEntity<SnsResponse> handler(Exception e) {
        List<String> errors = new ArrayList<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof ValidationErrorException) {
            errors = ((ValidationErrorException) e).getErrors();
        } else if(e instanceof OperationFailedException) {
            errors = new ArrayList<>();
            errors.add(e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        SnsResponse response = SnsResponse.builder()
                .message("Message failed")
                .errors(errors)
                .build();
        return new ResponseEntity<>(response, status);
    }
}
