package com.ll.tenmindaily.boundedContext.investment.Stocks.AmericaData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AmericaStockCrawlingService {
    public List<AmericaStockCrawlingData> getAmericaStockCrawlingData() throws IOException {
        List<AmericaStockCrawlingData> stocks = new ArrayList<>();

        String url = "https://www.slickcharts.com/sp500";
        Document document = Jsoup.connect(url).get();
        Element table = document.selectFirst("table.table.table-hover.table-borderless.table-sm");
        Element tbody = table.selectFirst("tbody");
        Elements rows = tbody.select("tr");

        for (Element row : rows) {
            Elements tds = row.select("td");
            Element td = tds.get(1);
            Element a = td.selectFirst("a");
            String ticker = a.attr("href").replace("/symbol/", "");
            String name = a.text();

            AmericaStockCrawlingData stock = new AmericaStockCrawlingData(ticker, name);
            stocks.add(stock);
        }

        return stocks;
    }
}