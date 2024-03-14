package com.quote.handler;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ApiException extends RuntimeException {

    private String code;

    private String message;

    public ApiException(String code, String message) {
        this.code = code;
        this.message = message;
    }

}