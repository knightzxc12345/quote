package com.design.controller.quote;

import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.quote.request.QuoteCreateRequest;
import com.design.controller.quote.request.QuoteFindRequest;
import com.design.controller.quote.request.QuoteUpdateRequest;
import com.design.controller.quote.response.QuoteFindAllResponse;
import com.design.controller.quote.response.QuoteFindPageResponse;
import com.design.controller.quote.response.QuoteFindResponse;
import com.design.usecase.quote.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/quote")
@RestController
@RequiredArgsConstructor
@Validated
public class QuoteController {

    private final QuoteCreateUseCase quoteCreateUseCase;

    private final QuoteUpdateUseCase quoteUpdateUseCase;

    private final QuoteDeleteUseCase quoteDeleteUseCase;

    private final QuoteFindUseCase quoteFindUseCase;

    private final QuoteFileUseCase quoteFileUseCase;

    @PostMapping(
            value = "v1"
    )
    public ResponseBody create(
            @RequestBody @Validated @NotNull final QuoteCreateRequest request) {
        quoteCreateUseCase.create(request);
        return new ResponseBody(CommonEnum.C00003);
    }

    @PutMapping(
            value = "v1/{quoteUuid}"
    )
    public ResponseBody update(
            @PathVariable("quoteUuid") @NotNull final String quoteUuid,
            @RequestBody @Validated @NotNull final QuoteUpdateRequest request) {
        quoteUpdateUseCase.update(request, quoteUuid);
        return new ResponseBody(CommonEnum.C00004);
    }

    @DeleteMapping(
            value = "v1/{quoteUuid}"
    )
    public ResponseBody delete(
            @PathVariable("quoteUuid") @NotNull final String quoteUuid) {
        quoteDeleteUseCase.delete(quoteUuid);
        return new ResponseBody(CommonEnum.C00005);
    }

    @GetMapping(
            value = "v1/{quoteUuid}"
    )
    public ResponseBody findByUuid(
            @PathVariable("quoteUuid") @NotNull final String quoteUuid) {
        QuoteFindResponse response = quoteFindUseCase.findByUuid(quoteUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1/preview/{quoteUuid}"
    )
    public void preview(
            @PathVariable("quoteUuid") @NotNull final String quoteUuid) {
        quoteFileUseCase.preview(quoteUuid);
    }

    @GetMapping(
            value = "v1"
    )
    public ResponseBody findAll(
            @Validated final QuoteFindRequest request) {
        if(null == request.page()){
            List<QuoteFindAllResponse> responses = quoteFindUseCase.findAll(request);
            return new ResponseBody(CommonEnum.C00002, responses);
        }
        QuoteFindPageResponse response = quoteFindUseCase.findAllByPage(request);
        return new ResponseBody(CommonEnum.C00002, response);
    }

}
