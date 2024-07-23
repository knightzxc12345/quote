package com.design.controller.quote.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record QuoteFindResponse(

        UUID quoteUUid,

        UUID userUuid,

        UUID customerUuid,

        String underTakerName,

        String underTakerTel,

        BigDecimal amount,

        BigDecimal tax,

        BigDecimal totalAmount,

        BigDecimal customAmount,

        BigDecimal customTax,

        BigDecimal customTotalAmount,

        BigDecimal costAmount,

        BigDecimal costTax,

        BigDecimal costTotalAmount,

        List<QuoteFindResponse.Item> items

) {

        public record Item(

                UUID itemUuid,

                String itemNo,

                String itemName,

                String itemSpec,

                String itemUnit,

                Integer quantity,

                BigDecimal itemVendorProductPrice,

                BigDecimal itemVendorProductAmount,

                BigDecimal itemVendorProductCustomPrice,

                BigDecimal itemVendorProductCustomAmount,

                BigDecimal itemVendorProductCostPrice,

                BigDecimal itemVendorProductCostAmount

        ){
        }

}
