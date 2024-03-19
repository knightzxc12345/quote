package com.quote.controller.quote;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/quote")
@RestController
@RequiredArgsConstructor
@Validated
public class QuoteController {



}
