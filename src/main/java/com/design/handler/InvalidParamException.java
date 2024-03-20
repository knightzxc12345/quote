package com.design.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;

@Setter
@Getter
public final class InvalidParamException extends RuntimeException {

    private Errors errors;

    public InvalidParamException(Errors errors) {
        this.errors = errors;
    }

}