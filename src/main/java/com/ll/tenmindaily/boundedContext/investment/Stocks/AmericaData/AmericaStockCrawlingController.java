package com.ll.tenmindaily.boundedContext.investment.Stocks.AmericaData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AmericaStockCrawlingController {
    private final AmericaStockCrawlingService americaStockCrawlingService;

    @Autowired
    public AmericaStockCrawlingController(AmericaStockCrawlingService americaStockCrawlingService) {
        this.americaStockCrawlingService = americaStockCrawlingService;
    }
}