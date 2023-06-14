package com.ll.tenmindaily.boundedContext.investment.Stocks.companyInfo;

import com.ll.tenmindaily.boundedContext.investment.Stocks.AmericaData.AmericaStockCrawlingData;
import com.ll.tenmindaily.boundedContext.investment.Stocks.AmericaData.AmericaStockCrawlingService;
import com.ll.tenmindaily.boundedContext.investment.Stocks.KoreaData.KoreaStockCrawlingData;
import com.ll.tenmindaily.boundedContext.investment.Stocks.KoreaData.KoreaStockCrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class StockScheduler {

    private final KoreaStockCrawlingService koreaCrawlingService;
    private final AmericaStockCrawlingService americaCrawlingService;
    private final StockService stockService;

    @Autowired
    public StockScheduler(KoreaStockCrawlingService koreaCrawlingService, AmericaStockCrawlingService americaCrawlingService, StockService stockService) {
        this.koreaCrawlingService = koreaCrawlingService;
        this.americaCrawlingService = americaCrawlingService;
        this.stockService = stockService;
    }

//    @Scheduled(fixedDelay = 1000000) // 1000초마다 실행
    public void updateStockData() throws IOException {

        // 미국 주식 데이터 가져오기
        List<AmericaStockCrawlingData> americaStocks = americaCrawlingService.getAmericaStockCrawlingData();

        updateAmericaStockData(americaStocks);

        // 한국 주식 데이터 가져오기
        List<KoreaStockCrawlingData> crawlingData0 = koreaCrawlingService.getKoreaStockCrawling(0); // sosok 값이 0일 때 데이터 가져오기
        List<KoreaStockCrawlingData> crawlingData1 = koreaCrawlingService.getKoreaStockCrawling(1); // sosok 값이 1일 때 데이터 가져오기

        updateStockDataWithSosok(crawlingData0, ".ks"); // sosok 값이 0일 때 .ks 접미사 추가
        updateStockDataWithSosok(crawlingData1, ".kq"); // sosok 값이 1일 때 .kq 접미사 추가

    }

    private void updateStockDataWithSosok(List<KoreaStockCrawlingData> crawlingData, String suffix) {
        for (KoreaStockCrawlingData data : crawlingData) {
            String symbol = data.getSymbol();
            String ticker = symbol + suffix;

            // 기존에 저장된 주식 정보 가져오기
            Stock existingStock = stockService.getStockBySymbol(symbol);

            // 주식 정보 업데이트하기
            try {
                String companyInfo = stockService.getYahooCompanyInfo(ticker);
                String targetInfo = stockService.getYahooFinancialData(ticker);
                String annInfo = stockService.getYahooAnalystPredictions(ticker);

                if (companyInfo != null && targetInfo != null && annInfo != null) {
                    if (existingStock != null) {
                        stockService.updateStock(symbol, companyInfo, targetInfo, annInfo);
                    } else {
                        stockService.saveStockCompanyData(symbol, companyInfo, targetInfo, annInfo);
                    }
                } else {
                    System.out.println("Skipping stock with symbol: " + symbol);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAmericaStockData(List<AmericaStockCrawlingData> americaStocks) {
        for (AmericaStockCrawlingData data : americaStocks) {
            String symbol = data.getSymbol();

            // 기존에 저장된 주식 정보 가져오기
            Stock existingStock = stockService.getStockBySymbol(symbol);

            // 주식 정보 업데이트하기
            try {
                String companyInfo = stockService.getYahooCompanyInfo(symbol);
                String targetInfo = stockService.getYahooFinancialData(symbol);
                String annInfo = stockService.getYahooAnalystPredictions(symbol);

                if (companyInfo != null && targetInfo != null && annInfo != null) {
                    if (existingStock != null) {
                        stockService.updateStock(symbol, companyInfo, targetInfo, annInfo);
                    } else {
                        stockService.saveStockCompanyData(symbol, companyInfo, targetInfo, annInfo);
                    }
                } else {
                    System.out.println("Skipping stock with symbol: " + symbol);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
