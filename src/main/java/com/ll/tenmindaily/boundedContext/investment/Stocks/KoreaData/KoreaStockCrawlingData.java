package com.ll.tenmindaily.boundedContext.investment.Stocks.KoreaData;

import lombok.Getter;

@Getter
public class KoreaStockCrawlingData {
    private String companyName;
    private String symbol;
    private int sosok;

    public KoreaStockCrawlingData(String companyName, String symbol, int sosok) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.sosok = sosok;
    }
}
