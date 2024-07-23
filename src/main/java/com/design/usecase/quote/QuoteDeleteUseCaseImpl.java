package com.design.usecase.quote;

import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuoteDeleteUseCaseImpl implements QuoteDeleteUseCase {

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    @Override
    public void delete(UUID quoteUuid) {
        // 取得報價單
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        // 取得報價單明細清單
        List<QuoteDetailEntity> quoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        // 刪除報價單
        quoteService.delete(quoteEntity, JwtUtil.extractUserUuid());
        // 刪除報價單明細
        quoteDetailService.deleteAll(quoteDetailEntities, JwtUtil.extractUserUuid());
    }

}
