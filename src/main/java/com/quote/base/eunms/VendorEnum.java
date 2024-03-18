package com.quote.base.eunms;

public enum VendorEnum implements ReturnEnum {

    VE0001("廠商已存在"),

    ;

    private String message;

    VendorEnum(String message) {
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
