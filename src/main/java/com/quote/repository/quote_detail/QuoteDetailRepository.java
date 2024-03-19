package com.quote.repository.quote_detail;

import com.quote.entity.quote_detail.QuoteDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteDetailRepository extends JpaRepository<QuoteDetailEntity, Long> {

    List<QuoteDetailEntity> findByIsDeletedFalseAndQuoteUuidOrderByIndexAsc(String quoteUuid);

}
