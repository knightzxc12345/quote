package com.design.entity.enums;

import com.design.converter.ConverterBase;

public enum QuoteStatus implements EnumBase<Integer> {

    CREATE(1, "建立"),

    FINISH(3, "完成")

    ;

    private int status;

    private String name;

    QuoteStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    @Override
    public Integer get() {
        return status;
    }

    public String getName() {
        return name;
    }

    public static QuoteStatus from(final Integer status) {
        if(null == status){
            return null;
        }
        for (QuoteStatus value : QuoteStatus.values()) {
            if (value.status == status) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid QuoteStatusEnum status : " + status);
    }

    public static class Converter extends ConverterBase<QuoteStatus, Integer> {
        public Converter() {
            super(QuoteStatus.class);
        }
    }

}
