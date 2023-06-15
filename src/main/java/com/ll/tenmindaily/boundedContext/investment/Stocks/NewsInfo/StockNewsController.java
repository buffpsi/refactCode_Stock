package com.ll.tenmindaily.boundedContext.investment.Stocks.NewsInfo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stocks")
public class StockNewsController {
    private final NewsService newsService;

    public StockNewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news/{symbol}")
    public String showStockNews(@PathVariable String symbol, Model model) {
        StockNewsItem[] newsItems = newsService.getStockNews(symbol);
        model.addAttribute("symbol", symbol);
        model.addAttribute("newsItems", newsItems);
        return "/usr/Stock/stockNews";
    }
}