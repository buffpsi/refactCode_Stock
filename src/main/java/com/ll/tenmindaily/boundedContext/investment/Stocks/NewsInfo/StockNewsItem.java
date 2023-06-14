package com.ll.tenmindaily.boundedContext.investment.Stocks.NewsInfo;

import lombok.Getter;

@Getter
public class StockNewsItem {
    private String pressName;
    private String newsTitle;
    private String description;
    private String url;

    public StockNewsItem(String pressName, String newsTitle, String description, String url) {
        this.pressName = pressName;
        this.newsTitle = newsTitle;
        this.description = description;
        this.url = url;
    }
}