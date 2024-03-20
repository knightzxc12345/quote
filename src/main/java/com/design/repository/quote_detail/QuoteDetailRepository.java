package com.design.repository.quote_detail;

import com.design.entity.quote_detail.QuoteDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteDetailRepository extends JpaRepository<QuoteDetailEntity, Long> {

    List<QuoteDetailEntity> findByIsDeletedFalseAndQuoteUuidOrderByPkAsc(String quoteUuid);

}
