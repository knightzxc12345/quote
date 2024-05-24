package com.design.controller.vendor_quote;

import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.vendor_quote.request.VendorQuoteRequest;
import com.design.controller.vendor_quote.response.VendorQuoteFindAllResponse;
import com.design.controller.vendor_quote.response.VendorQuoteFindPageResponse;
import com.design.controller.vendor_quote.response.VendorQuoteFindResponse;
import com.design.usecase.vendor_quote.VendorQuoteFindUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/vendor-quote")
@RestController
@RequiredArgsConstructor
@Validated
public class VendorQuoteController {

    private final VendorQuoteFindUseCase vendorQuoteFindUseCase;

    @GetMapping(
            value = "v1/{vendorQuoteUuid}"
    )
    public ResponseBody findByUuid(
            @PathVariable("vendorQuoteUuid") @NotNull final String vendorQuoteUuid) {
        VendorQuoteFindResponse response = vendorQuoteFindUseCase.findByUuid(vendorQuoteUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1"
    )
    public ResponseBody findAll(
            @Validated final VendorQuoteRequest request) {
        if(null == request.page() || null == request.size()){
            List<VendorQuoteFindAllResponse> responses = vendorQuoteFindUseCase.findAll(request);
            return new ResponseBody(CommonEnum.C00002, responses);
        }
        VendorQuoteFindPageResponse response = vendorQuoteFindUseCase.findAllByPage(request);
        return new ResponseBody(CommonEnum.C00002, response);
    }

}
