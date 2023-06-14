package com.ll.tenmindaily.boundedContext.investment.Stocks.NewsInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {
    public StockNewsItem[] getStockNews(String symbol) {
        List<StockNewsItem> newsItems = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://search.naver.com/search.naver?query=" + symbol + "&where=news").get();
            Elements items = doc.select("li.bx");
            for (Element item : items) {
                Element pressElement = item.selectFirst(".info_group .info.press");
                if (pressElement == null) {
                    continue;
                }
                String pressName = pressElement.text();
                Element titleElement = item.selectFirst("a.news_tit");
                String newsTitle = titleElement != null ? titleElement.text() : "";
                Element descriptionElement = item.selectFirst(".news_dsc .dsc_wrap");
                String description = descriptionElement != null ? descriptionElement.text() : "";
                Element urlElement = item.selectFirst("a.news_tit");
                String url = urlElement != null ? urlElement.attr("href") : "";

                if (pressName.isEmpty() || newsTitle.isEmpty() || url.isEmpty() || description.isEmpty()) {
                    continue;
                }

                StockNewsItem newsItem = new StockNewsItem(pressName, newsTitle, description, url);
                newsItems.add(newsItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsItems.toArray(new StockNewsItem[0]);
    }
}
