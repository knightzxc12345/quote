package com.design.controller.quote;

import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.quote.request.QuoteCreateRequest;
import com.design.controller.quote.request.QuoteFindRequest;
import com.design.controller.quote.request.QuoteUpdateRequest;
import com.design.controller.quote.response.QuoteFindAllResponse;
import com.design.controller.quote.response.QuoteFindPageResponse;
import com.design.controller.quote.response.QuoteFindResponse;
import com.design.controller.quote.response.QuotePreviewResponse;
import com.design.usecase.quote.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
            @RequestBody @Validated @NotNull QuoteCreateRequest request) {
        quoteCreateUseCase.create(request);
        return new ResponseBody(CommonEnum.C00003);
    }

    @PutMapping(
            value = "v1/{quoteUuid}"
    )
    public ResponseBody update(
            @PathVariable("quoteUuid") @NotNull UUID quoteUuid,
            @RequestBody @Validated @NotNull QuoteUpdateRequest request) {
        quoteUpdateUseCase.update(request, quoteUuid);
        return new ResponseBody(CommonEnum.C00004);
    }

    @DeleteMapping(
            value = "v1/{quoteUuid}"
    )
    public ResponseBody delete(
            @PathVariable("quoteUuid") @NotNull UUID quoteUuid) {
        quoteDeleteUseCase.delete(quoteUuid);
        return new ResponseBody(CommonEnum.C00005);
    }

    @GetMapping(
            value = "v1/{quoteUuid}"
    )
    public ResponseBody findByUuid(
            @PathVariable("quoteUuid") @NotNull UUID quoteUuid) {
        QuoteFindResponse response = quoteFindUseCase.findByUuid(quoteUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1/preview/{quoteUuid}"
    )
    public ResponseBody preview(
            @PathVariable("quoteUuid") @NotNull UUID quoteUuid) {
        QuotePreviewResponse response = quoteFileUseCase.preview(quoteUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1/download/{quoteUuid}"
    )
    public void download(
            @PathVariable("quoteUuid") @NotNull UUID quoteUuid) {
        quoteFileUseCase.download(quoteUuid);
    }

    @GetMapping(
            value = "v1"
    )
    public ResponseBody findAll(
            @Validated QuoteFindRequest request) {
        if(null == request.page() || null == request.size()){
            List<QuoteFindAllResponse> responses = quoteFindUseCase.findAll(request);
            return new ResponseBody(CommonEnum.C00002, responses);
        }
        QuoteFindPageResponse response = quoteFindUseCase.findAllByPage(request);
        return new ResponseBody(CommonEnum.C00002, response);
    }

}
