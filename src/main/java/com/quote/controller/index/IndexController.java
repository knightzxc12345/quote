package com.quote.controller.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/customer")
    public String customer() {
        return "customer";
    }

    @RequestMapping("/product")
    public String product() {
        return "product";
    }

    @RequestMapping("/quote")
    public String quote() {
        return "quote";
    }

}
