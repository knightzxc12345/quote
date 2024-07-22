package com.design.modle;

import lombok.Data;

@Data
public class QuoteDetail {

    private Integer index;

    private String itemNo;

    private String itemName;

    private String itemSpec;

    private String quantity;

    private String itemUnit;

    private String itemVendorProductCustomPrice;

    private String itemVendorProductCustomAmount;

}