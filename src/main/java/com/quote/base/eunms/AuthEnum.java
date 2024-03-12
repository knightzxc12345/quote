package com.quote.base.eunms;

public enum AuthEnum implements ReturnEnum {

    A00001("Unauthorized"),

    A00002("HTTP Method 錯誤"),

    A00003("缺少必要的Request Header"),

    A00004("參數錯誤"),

    A00005("系統錯誤"),

    A00006("token過期"),

    ;

    private String message;

    AuthEnum(String message) {
        this.message = message;
    }

    @Override
    public String key() {
        return this.name();
    }

    @Override
    public String value() {
        return message;
    }
}