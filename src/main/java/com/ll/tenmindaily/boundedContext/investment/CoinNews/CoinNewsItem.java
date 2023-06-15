package com.ll.tenmindaily.boundedContext.investment.CoinNews;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinNewsItem {
    private String title;
    private String lead;
    private String url;
    private String dateTime;

    public CoinNewsItem(String title, String lead, String url, String dateTime) {
        this.title = title;
        this.lead = lead;
        this.url = url;
        this.dateTime = dateTime;
    }
}
