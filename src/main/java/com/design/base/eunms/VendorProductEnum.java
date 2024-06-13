package com.design.base.eunms;

public enum VendorProductEnum implements ReturnEnum {

    VE0001("廠商產品已存在"),

    ;

    private String message;

    VendorProductEnum(String message) {
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
