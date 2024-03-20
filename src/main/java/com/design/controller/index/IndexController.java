package com.design.controller.index;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/customer")
    public String customer() {
        return "customer";
    }

    @RequestMapping("/vendor")
    public String vendor() {
        return "vendor";
    }

    @RequestMapping("/item")
    public String item() {
        return "item";
    }

    @RequestMapping("/product")
    public String product() {
        return "product";
    }

    @RequestMapping("/quote")
    public String quote() {
        return "quote";
    }

    @RequestMapping("/quote/create")
    public String quoteCreate() {
        return "quote-create";
    }

    @RequestMapping("/quote/update/{quoteUuid}")
    public String quoteUpdate(
            @PathVariable("quoteUuid") @NotNull final String quoteUuid,
            Model model) {
        return "quote-update";
    }

}
