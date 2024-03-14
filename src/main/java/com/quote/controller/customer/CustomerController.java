package com.quote.controller.customer;

import com.quote.base.ResponseBody;
import com.quote.base.eunms.CommonEnum;
import com.quote.controller.customer.request.CustomerCreateRequest;
import com.quote.controller.customer.request.CustomerFindRequest;
import com.quote.controller.customer.request.CustomerUpdateRequest;
import com.quote.controller.customer.response.CustomerFindAllResponse;
import com.quote.controller.customer.response.CustomerFindPageResponse;
import com.quote.controller.customer.response.CustomerFindResponse;
import com.quote.usecase.customer.CustomerCreateUseCase;
import com.quote.usecase.customer.CustomerDeleteUseCase;
import com.quote.usecase.customer.CustomerFindUseCase;
import com.quote.usecase.customer.CustomerUpdateUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerCreateUseCase customerCreateUseCase;

    private final CustomerUpdateUseCase customerUpdateUseCase;

    private final CustomerDeleteUseCase customerDeleteUseCase;

    private final CustomerFindUseCase customerFindUseCase;

    @PostMapping(
            value = "v1"
    )
    public ResponseBody create(
            @RequestBody @Validated @NotNull final CustomerCreateRequest request) {
        customerCreateUseCase.create(request);
        return new ResponseBody(CommonEnum.C00003);
    }

    @PutMapping(
            value = "v1/{customerUuid}"
    )
    public ResponseBody update(
            @PathVariable("customerUuid") @NotNull final String customerUuid,
            @RequestBody @Validated @NotNull final CustomerUpdateRequest request) {
        customerUpdateUseCase.update(request, customerUuid);
        return new ResponseBody(CommonEnum.C00004);
    }

    @DeleteMapping(
            value = "v1/{customerUuid}"
    )
    public ResponseBody delete(
            @PathVariable("customerUuid") @NotNull final String customerUuid) {
        customerDeleteUseCase.delete(customerUuid);
        return new ResponseBody(CommonEnum.C00005);
    }

    @GetMapping(
            value = "v1/{customerUuid}"
    )
    public ResponseBody findByUuid(
            @PathVariable("customerUuid") @NotNull final String customerUuid) {
        CustomerFindResponse response = customerFindUseCase.findByUuid(customerUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1"
    )
    public ResponseBody findAll(
            @Validated final CustomerFindRequest request) {
        if(null == request.page()){
            List<CustomerFindAllResponse> responses = customerFindUseCase.findAll(request);
            return new ResponseBody(CommonEnum.C00002, responses);
        }
        CustomerFindPageResponse response = customerFindUseCase.findAllByPage(request);
        return new ResponseBody(CommonEnum.C00002, response);
    }

}
