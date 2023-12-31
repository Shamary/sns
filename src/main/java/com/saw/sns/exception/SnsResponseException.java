package com.saw.sns.exception;

import com.saw.sns.common.SnsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SnsResponseException extends RuntimeException {
    private final List<String> errors;
    private SnsResponse response;

    public SnsResponseException(List<String> errors) {
        super("Validation errors occurred.");
        this.errors = errors;
        this.response = SnsResponse.builder()
                .message("Message Failed")
                .errors(errors)
                .build();
    }

    public List<String> getErrors() {
        return errors;
    }

    public SnsResponse getResponse() {
        return this.response;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());
        sb.append("\n");
        for (String error : errors) {
            sb.append("- ").append(error).append("\n");
        }
        return sb.toString();
    }
}
