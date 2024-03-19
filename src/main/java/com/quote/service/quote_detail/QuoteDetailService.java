package com.quote.service.quote_detail;

import com.quote.entity.quote_detail.QuoteDetailEntity;

import java.util.List;

public interface QuoteDetailService {

    void createAll(List<QuoteDetailEntity> quoteDetailEntities, String userUuid);

    void updateAll(List<QuoteDetailEntity> quoteDetailEntities, String userUuid);

    void deleteAll(List<QuoteDetailEntity> quoteDetailEntities, String userUuid);

    List<QuoteDetailEntity> findAll(String quoteUuid);

}
