package com.design.service.quote_detail;

import com.design.entity.quote_detail.QuoteDetailEntity;

import java.util.List;
import java.util.UUID;

public interface QuoteDetailService {

    void createAll(List<QuoteDetailEntity> quoteDetailEntities, UUID userUuid);

    void updateAll(List<QuoteDetailEntity> quoteDetailEntities, UUID userUuid);

    void deleteAll(List<QuoteDetailEntity> quoteDetailEntities, UUID userUuid);

    List<QuoteDetailEntity> findAll(UUID quoteUuid);

}
