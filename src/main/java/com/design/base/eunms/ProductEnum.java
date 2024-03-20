package com.design.base.eunms;

public enum ProductEnum implements ReturnEnum {

    PR0001("產品已存在"),

    ;

    private String message;

    ProductEnum(String message) {
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
