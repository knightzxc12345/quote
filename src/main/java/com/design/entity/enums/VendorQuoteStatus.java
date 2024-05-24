package com.design.entity.enums;

import com.design.converter.ConverterBase;

public enum VendorQuoteStatus implements EnumBase<Integer> {

    CREATE(1, "建立"),

    FINISH(3, "完成")

    ;

    private int status;

    private String value;

    VendorQuoteStatus(int status, String value) {
        this.status = status;
        this.value = value;
    }

    @Override
    public Integer get() {
        return status;
    }

    public String getValue(){
        return value;
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
