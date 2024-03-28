package com.design.base.eunms;

public enum ExcelEnum implements ReturnEnum {

    E00001("匯出excel錯誤"),

    E00002("查無原始檔案"),

    E00003("取得物件欄位錯誤"),

    E00004("欄位轉字串錯誤"),

    E00005("Excel設定失敗"),

    E00006("Excel轉換失敗"),

    ;

    private String message;

    ExcelEnum(String message) {
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