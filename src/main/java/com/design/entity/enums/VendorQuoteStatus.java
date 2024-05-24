package com.design.entity.enums;

import com.design.converter.ConverterBase;

public enum VendorQuoteStatus implements EnumBase<Integer> {

    // 建立
    CREATE(1),

    // 完成
    FINISH(3)

    ;

    private final int status;

    VendorQuoteStatus(final int status) {
        this.status = status;
    }

    @Override
    public Integer get() {
        return status;
    }

    public static VendorQuoteStatus from(final Integer status) {
        if(null == status){
            return null;
        }
        for (VendorQuoteStatus value : VendorQuoteStatus.values()) {
            if (value.status == status) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid VendorQuoteStatus status : " + status);
    }

    public static class Converter extends ConverterBase<VendorQuoteStatus, Integer> {
        public Converter() {
            super(VendorQuoteStatus.class);
        }
    }

}
