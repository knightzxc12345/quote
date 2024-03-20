package com.design.entity.enums;

import com.design.converter.ConverterBase;

public enum QuoteStatus implements EnumBase<Integer> {

    // 建立
    CREATE(1),

    // 完成
    FINISH(3)

    ;

    private final int status;

    QuoteStatus(final int status) {
        this.status = status;
    }

    @Override
    public Integer get() {
        return status;
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
