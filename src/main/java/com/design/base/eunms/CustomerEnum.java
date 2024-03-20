package com.design.base.eunms;

public enum CustomerEnum implements ReturnEnum {

    CU0001("客戶已存在"),

    ;

    private String message;

    CustomerEnum(String message) {
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
