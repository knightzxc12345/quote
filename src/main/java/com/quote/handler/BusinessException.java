package com.quote.handler;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BusinessException extends RuntimeException {

    private Object status;

    public BusinessException(Object status) {
        this.status = status;
    }

}