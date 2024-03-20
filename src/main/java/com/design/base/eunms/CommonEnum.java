package com.design.base.eunms;

public enum CommonEnum implements ReturnEnum {

    C00001("查詢成功"),

    C00002("查詢清單成功"),

    C00003("新增成功"),

    C00004("編輯成功"),

    C00005("刪除成功"),

    ;

    private String message;

    CommonEnum(String message) {
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
