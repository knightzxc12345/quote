package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteFindRequest;
import com.design.controller.quote.response.QuoteFindAllResponse;
import com.design.controller.quote.response.QuoteFindPageResponse;
import com.design.controller.quote.response.QuoteFindResponse;

import java.util.List;

public interface QuoteFindUseCase {

    QuoteFindResponse findByUuid(String quoteUuid);

    List<QuoteFindAllResponse> findAll(QuoteFindRequest request);

    QuoteFindPageResponse findAllByPage(QuoteFindRequest request);

}
