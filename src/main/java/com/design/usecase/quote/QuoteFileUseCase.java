package com.design.usecase.quote;

import com.design.controller.quote.response.QuotePreviewResponse;

public interface QuoteFileUseCase {

    QuotePreviewResponse preview(String quoteUuid);

    void download(String quoteUuid);

}
