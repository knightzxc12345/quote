package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteFindRequest;
import com.design.controller.quote.response.QuoteFindAllResponse;
import com.design.controller.quote.response.QuoteFindPageResponse;
import com.design.controller.quote.response.QuoteFindResponse;
import com.design.entity.quote.QuoteEntity;
import com.design.service.quote.QuoteService;
import com.design.utils.InstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteFindUseCaseImpl implements QuoteFindUseCase {

    private final QuoteService quoteService;

    @Override
    public QuoteFindResponse findByUuid(String quoteUuid) {
        return null;
    }

    @Override
    public List<QuoteFindAllResponse> findAll(QuoteFindRequest request) {
        List<QuoteEntity> quoteEntities = quoteService.findAllLike(
                request.userUuid(),
                request.customerUuid(),
                request.keyword()
        );
        return format(quoteEntities);
    }

    @Override
    public QuoteFindPageResponse findAllByPage(QuoteFindRequest request) {
        Integer page = request.page();
        Integer size = null == request.size() ? 10 : request.size();
        Sort.Order orderCreateTime = new Sort.Order(Sort.Direction.DESC, "createTime");
        Sort.Order orderUserUuid = new Sort.Order(Sort.Direction.ASC, "userUuid");
        Sort.Order orderCustomerUuid = new Sort.Order(Sort.Direction.ASC, "customerUuid");
        Sort sort = Sort.by(orderCreateTime, orderUserUuid, orderCustomerUuid);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<QuoteEntity> quoteEntityPage = quoteService.findAllLikeByPage(
                request.userUuid(),
                request.customerUuid(),
                request.keyword(),
                pageable
        );
        List<QuoteFindAllResponse> responses = format(quoteEntityPage.getContent());
        return new QuoteFindPageResponse(
                quoteEntityPage.getTotalPages(),
                quoteEntityPage.getNumber(),
                quoteEntityPage.getSize(),
                responses
        );
    }

    private List<QuoteFindAllResponse> format(List<QuoteEntity> quoteEntities){
        List<QuoteFindAllResponse> responses = new ArrayList<>();
        if(null == quoteEntities || quoteEntities.isEmpty()){
            return responses;
        }
        for(QuoteEntity quoteEntity : quoteEntities){
            responses.add(new QuoteFindAllResponse(
                    quoteEntity.getUuid(),
                    quoteEntity.getUserUuid(),
                    quoteEntity.getCustomerUuid(),
                    quoteEntity.getTotalAmount(),
                    quoteEntity.getCustomerTotalAmount(),
                    quoteEntity.getCostTotalAmount(),
                    InstantUtil.to(quoteEntity.getCreateTime()),
                    quoteEntity.getQuoteStatus().get()
            ));
        }
        return responses;
    }

}
