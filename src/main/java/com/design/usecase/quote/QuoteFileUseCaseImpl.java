package com.design.usecase.quote;

import com.design.base.Common;
import com.design.base.eunms.AuthEnum;
import com.design.controller.quote.response.QuotePreviewResponse;
import com.design.entity.customer.CustomerEntity;
import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.entity.user.UserEntity;
import com.design.handler.BusinessException;
import com.design.modle.QuoteDetail;
import com.design.service.customer.CustomerService;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.service.user.UserService;
import com.design.utils.ExcelUtil;
import com.design.utils.HttpUtil;
import com.design.utils.InstantUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuoteFileUseCaseImpl implements QuoteFileUseCase {

    @Value("classpath:files/quote-01.xlsx")
    private Resource quote01Resource;

    @Value("classpath:files/quote-02.xlsx")
    private Resource quote02Resource;

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    private final CustomerService customerService;

    private final UserService userService;

    @Override
    public QuotePreviewResponse preview(UUID quoteUuid) {
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        CustomerEntity customerEntity = customerService.findByUuid(quoteEntity.getCustomerUuid());
        UserEntity userEntity = userService.findByUuid(quoteEntity.getUserUuid());
        List<QuoteDetailEntity> quoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        List<QuotePreviewResponse.Item> items = getItems(quoteDetailEntities);
        return new QuotePreviewResponse(
                quoteEntity.getUuid(),
                userEntity.getName(),
                customerEntity.getName(),
                customerEntity.getAddress(),
                quoteEntity.getUnderTakerName(),
                quoteEntity.getUnderTakerTel(),
                quoteEntity.getAmount(),
                quoteEntity.getTax(),
                quoteEntity.getTotalAmount(),
                quoteEntity.getCustomAmount(),
                quoteEntity.getCustomTax(),
                quoteEntity.getCustomTotalAmount(),
                quoteEntity.getCostAmount(),
                quoteEntity.getCostTax(),
                quoteEntity.getCostTotalAmount(),
                items
        );
    }

    @Override
    public void download(UUID quoteUuid, Integer company) {
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        List<QuoteDetailEntity> quoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        // 取得寫入excel陣列資料
        List<QuoteDetail> quoteDetails = getQuoteDetails(quoteDetailEntities);
        // 取得寫入資料
        Map<String, String> params = getParams(quoteEntity);
        // 取得excel原始檔
        InputStream inputStream = getResource(company);
        // 寫入excel
        InputStream quoteInputStream = ExcelUtil.create(inputStream, quoteDetails, params);
        writeFile(quoteInputStream);
    }

    private Map<String, String> getParams(QuoteEntity quoteEntity){
        UserEntity userEntity = userService.findByUuid(quoteEntity.getUserUuid());
        Map<String, String> params = new HashMap<>();
        params.put("userName", quoteEntity.getUserName());
        params.put("userMobile", userEntity.getMobile());
        params.put("customerName", quoteEntity.getCustomerName());
        params.put("customerAddress", quoteEntity.getCustomerAddress());
        params.put("underTakerName", quoteEntity.getUnderTakerName());
        params.put("createTime", InstantUtil.to(quoteEntity.getCreateTime(), Common.DATE_FORMAT_2));
        params.put("underTakerTel", quoteEntity.getUnderTakerTel());
        params.put("customAmount", Common.DECIMAL_FORMAT.format(quoteEntity.getCustomAmount()));
        params.put("customTax", Common.DECIMAL_FORMAT.format(quoteEntity.getCustomTax()));
        params.put("customTotalAmount", Common.DECIMAL_FORMAT.format(quoteEntity.getCustomTotalAmount()));
        return params;
    }

    // 取得來源
    private InputStream getResource(Integer company){
        if(1 == company){
            return getInputStream(quote01Resource);
        }
        if(2 == company){
            return getInputStream(quote02Resource);
        }
        return null;
    }

    private List<QuotePreviewResponse.Item> getItems(List<QuoteDetailEntity> quoteDetailEntities){
        List<QuotePreviewResponse.Item> items = new ArrayList<>();
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return items;
        }
        int i = 0;
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            items.add(new QuotePreviewResponse.Item(
                    ++i,
                    quoteDetailEntity.getItemNo(),
                    quoteDetailEntity.getItemName(),
                    quoteDetailEntity.getItemSpec(),
                    quoteDetailEntity.getItemUnit(),
                    quoteDetailEntity.getQuantity(),
                    quoteDetailEntity.getItemVendorProductPrice(),
                    quoteDetailEntity.getItemVendorProductAmount(),
                    quoteDetailEntity.getItemVendorProductCustomPrice(),
                    quoteDetailEntity.getItemVendorProductCustomAmount(),
                    quoteDetailEntity.getItemVendorProductCostPrice(),
                    quoteDetailEntity.getItemVendorProductCostAmount()
            ));
        }
        return items;
    }

    private List<QuoteDetail> getQuoteDetails(List<QuoteDetailEntity> quoteDetailEntities){
        List<QuoteDetail> quoteDetails = new ArrayList<>();
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return quoteDetails;
        }
        QuoteDetail quoteDetail;
        Integer index = 1;
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            quoteDetail = new QuoteDetail();
            quoteDetail.setIndex(index++);
            quoteDetail.setItemNo(quoteDetailEntity.getItemNo());
            quoteDetail.setItemName(quoteDetailEntity.getItemName());
            quoteDetail.setItemSpec(quoteDetailEntity.getItemSpec());
            quoteDetail.setQuantity(quoteDetail.getQuantity());
            quoteDetail.setItemUnit(quoteDetail.getItemUnit());
            quoteDetail.setItemVendorProductCustomPrice(Common.DECIMAL_FORMAT.format(quoteDetailEntity.getItemVendorProductCustomPrice()));
            quoteDetail.setItemVendorProductCustomAmount(Common.DECIMAL_FORMAT.format(quoteDetailEntity.getItemVendorProductCustomAmount()));
            quoteDetails.add(quoteDetail);
        }
        return quoteDetails;
    }

    // 讀取來源
    private InputStream getInputStream(Resource resource){
        try{
            return resource.getInputStream();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(AuthEnum.A00005);
        }
    }

    private void writeFile(InputStream inputStream){
        try{
            HttpServletResponse response = HttpUtil.getResponse();
            response.setContentType(Common.EXCEL_CONTENT_TYPE);
            response.setHeader("Content-Disposition", "attachment;");
            byte[] buffer = new byte[1024];
            int bytesRead;
            ServletOutputStream outputStream = response.getOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(AuthEnum.A00005);
        }
    }

}
