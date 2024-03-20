package com.design.controller.item;

import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.item.request.ItemCreateRequest;
import com.design.controller.item.request.ItemFindRequest;
import com.design.controller.item.request.ItemUpdateRequest;
import com.design.controller.item.response.ItemFindAllResponse;
import com.design.controller.item.response.ItemFindPageResponse;
import com.design.controller.item.response.ItemFindResponse;
import com.design.usecase.item.ItemCreateUseCase;
import com.design.usecase.item.ItemDeleteUseCase;
import com.design.usecase.item.ItemFindUseCase;
import com.design.usecase.item.ItemUpdateUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemCreateUseCase itemCreateUseCase;

    private final ItemUpdateUseCase itemUpdateUseCase;

    private final ItemDeleteUseCase itemDeleteUseCase;

    private final ItemFindUseCase itemFindUseCase;

    @PostMapping(
            value = "v1"
    )
    public ResponseBody create(
            @RequestBody @Validated @NotNull final ItemCreateRequest request) {
        itemCreateUseCase.create(request);
        return new ResponseBody(CommonEnum.C00003);
    }

    @PutMapping(
            value = "v1/{itemUuid}"
    )
    public ResponseBody update(
            @PathVariable("itemUuid") @NotNull final String itemUuid,
            @RequestBody @Validated @NotNull final ItemUpdateRequest request) {
        itemUpdateUseCase.update(request, itemUuid);
        return new ResponseBody(CommonEnum.C00004);
    }

    @DeleteMapping(
            value = "v1/{itemUuid}"
    )
    public ResponseBody delete(
            @PathVariable("itemUuid") @NotNull final String itemUuid) {
        itemDeleteUseCase.delete(itemUuid);
        return new ResponseBody(CommonEnum.C00005);
    }

    @GetMapping(
            value = "v1/{itemUuid}"
    )
    public ResponseBody findByUuid(
            @PathVariable("itemUuid") @NotNull final String itemUuid) {
        ItemFindResponse response = itemFindUseCase.findByUuid(itemUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1"
    )
    public ResponseBody findAll(
            @Validated final ItemFindRequest request) {
        if(null == request.page()){
            List<ItemFindAllResponse> responses = itemFindUseCase.findAll(request);
            return new ResponseBody(CommonEnum.C00002, responses);
        }
        ItemFindPageResponse response = itemFindUseCase.findAllByPage(request);
        return new ResponseBody(CommonEnum.C00002, response);
    }

}
