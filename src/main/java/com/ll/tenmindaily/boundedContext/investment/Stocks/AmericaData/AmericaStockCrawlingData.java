package com.ll.tenmindaily.boundedContext.investment.Stocks.AmericaData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmericaStockCrawlingData {
    private String symbol;
    private String name;

    public AmericaStockCrawlingData(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

}
