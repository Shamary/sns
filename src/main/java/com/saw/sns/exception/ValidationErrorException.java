package com.saw.sns.exception;

import java.util.List;

public class ValidationErrorException extends RuntimeException {
    private final List<String> errors;

    public ValidationErrorException(List<String> errors) {
        super("Validation errors occurred.");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
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

