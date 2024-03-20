package com.design.base;

import com.design.base.eunms.ReturnEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBody<T> {

    private String code;

    private String message;

    private T data;

    public ResponseBody(@NotNull final ReturnEnum status){
        this.code = status.key();
        this.message = status.value();
    }

    public ResponseBody(@NotNull final ReturnEnum status, T data){
        this.code = status.key();
        this.message = status.value();
        this.data = data;
    }

    public ResponseBody(@NotNull final String code, @NotNull final String message){
        this.code = code;
        this.message = message;
    }

}