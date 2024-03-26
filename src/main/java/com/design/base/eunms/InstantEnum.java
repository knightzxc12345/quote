package com.design.base.eunms;

public enum InstantEnum implements ReturnEnum {

    I00001("日期格式錯誤"),

    ;

    private String message;

    InstantEnum(String message) {
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
