package com.design.base.eunms;

public enum ItemEnum implements ReturnEnum {

    IT0001("品項已存在"),

    ;

    private String message;

    ItemEnum(String message) {
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
