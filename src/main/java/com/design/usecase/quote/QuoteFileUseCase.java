package com.design.usecase.quote;

import com.design.controller.quote.response.QuotePreviewResponse;

import java.util.UUID;

public interface QuoteFileUseCase {

    QuotePreviewResponse preview(UUID quoteUuid);

    void download(UUID quoteUuid, Integer company);

}
