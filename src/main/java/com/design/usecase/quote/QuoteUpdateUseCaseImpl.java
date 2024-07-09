package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuoteUpdateUseCaseImpl implements QuoteUpdateUseCase {

    @Override
    public void update(QuoteUpdateRequest request, UUID quoteUuid) {

    }

}
