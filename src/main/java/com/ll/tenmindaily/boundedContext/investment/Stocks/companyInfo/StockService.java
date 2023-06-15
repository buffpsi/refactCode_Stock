package com.ll.tenmindaily.boundedContext.investment.Stocks.companyInfo;

import com.ll.tenmindaily.base.rsData.RsData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
public class StockService {

    private StockRepository stockRepository;
    private final WebClient webClient;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.webClient = WebClient.create();
    }

    private final String yahooApiBaseUrl = "https://query1.finance.yahoo.com";

    // 야후 파이낸스 회사 api 정보
    public String getYahooCompanyInfo(String symbol) throws IOException {
        String endpoint = "/v10/finance/quoteSummary";
        String modules = "summaryDetail";
        String url = yahooApiBaseUrl + endpoint + "/" + symbol + "?modules=" + modules;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty()) // 에러가 404일 경우에는 empty Mono 반환
                .block();
    }

    // 야후 파이낸스 API를 통해 애널리스트들의 예측 정보를 가져오는 메서드
    public String getYahooAnalystPredictions(String symbol) throws IOException {
        String endpoint = "/v10/finance/quoteSummary";
        String modules = "recommendationTrend";
        String url = yahooApiBaseUrl + endpoint + "/" + symbol + "?modules=" + modules;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty()) // 에러가 404일 경우에는 empty Mono 반환
                .block();
    }

    // 야후 파이낸스 API를 통해 주식의 예측가격 데이터를 가져오는 메서드
    public String getYahooFinancialData(String symbol) throws IOException {
        String endpoint = "/v10/finance/quoteSummary";
        String modules = "financialData";
        String url = yahooApiBaseUrl + endpoint + "/" + symbol + "?modules=" + modules;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty()) // 에러가 404일 경우에는 empty Mono 반환
                .block();
    }


    // 컨트롤러에서 받아온 티커를 DB에 저장하는 메서드
    // 0번 실패 코드, 1번 성공 코드, 2번 중복 코드
    public RsData<Integer> saveStockCompanyData(String symbol, String companyInfo, String targetInfo, String annInfo) {
        Stock existingStock = stockRepository.findBySymbol(symbol);

        if (existingStock != null) {
            return RsData.failOf(2);  // 중복된 주식을 추가한 경우
        }

        Stock stock = yahooParseStockCompanyData(companyInfo, targetInfo, annInfo);
        if (stock == null) {
            return RsData.failOf(0);  // 주식 정보 파싱에 실패한 경우
        }

        stock.setSymbol(symbol);  // 종목 심볼 설정
        stock.setNational(symbol.endsWith(".ks") || symbol.endsWith(".kq") ? 0 : 1); //코스피 코스닥은 한국 주식으로
        stockRepository.save(stock);

        return RsData.successOf(1);  // 주식 추가 성공
    }

    public Stock yahooParseStockCompanyData(String companyInfo, String targetInfo, String annInfo) {
        try {
            Stock stock = new Stock();

            if (companyInfo != null) {
                JSONObject stockDataObject = new JSONObject(companyInfo);
                JSONObject quoteSummaryObject = stockDataObject.getJSONObject("quoteSummary");
                JSONArray resultArray = quoteSummaryObject.getJSONArray("result");

                if (resultArray != null && !resultArray.isNull(0)) {
                    JSONObject summaryDetailObject = resultArray.getJSONObject(0).getJSONObject("summaryDetail");

                    // 필요한 데이터 추출
                    double previousClose = summaryDetailObject.getJSONObject("previousClose").getDouble("raw");
                    // 아래의 배당정보들은 없을 수도 있으니까 제외할 수 있도록 ==> null값으로 넣을 수 있게
                    // 배당 정보 추출
                    Double dividendRate = summaryDetailObject.getJSONObject("dividendRate").optDouble("raw");
                    Double dividendYield = summaryDetailObject.getJSONObject("dividendYield").optDouble("raw");
                    String exDividendDate = summaryDetailObject.getJSONObject("exDividendDate").optString("fmt");
                    //시총도없을 수 있음 지수로 하면
                    Double marketCap = summaryDetailObject.getJSONObject("marketCap").optDouble("raw");

                    Double forwardPE = summaryDetailObject.getJSONObject("forwardPE").optDouble("raw");

                    double fiftyTwoWeekLow = summaryDetailObject.getJSONObject("fiftyTwoWeekLow").getDouble("raw");
                    double fiftyTwoWeekHigh = summaryDetailObject.getJSONObject("fiftyTwoWeekHigh").getDouble("raw");
                    double fiftyDayAverage = summaryDetailObject.getJSONObject("fiftyDayAverage").getDouble("raw");

                    Double priceToSalesTrailing12Months = summaryDetailObject.getJSONObject("priceToSalesTrailing12Months").optDouble("raw");
                    double twoHundredDayAverage = summaryDetailObject.getJSONObject("twoHundredDayAverage").getDouble("raw");

                    // Stock 객체 설정
                    stock.setPreviousClose(previousClose);
                    if (dividendRate.isNaN()) {
                        stock.setDividendRate(null);
                        stock.setDividendYield(null);
                        stock.setExDividendDate(null);

                    } else {
                        stock.setDividendRate(dividendRate);
                        stock.setDividendYield(dividendYield);
                        stock.setExDividendDate(exDividendDate);
                    }

                    if (forwardPE.isNaN()) {
                        stock.setForwardPE(null);
                    } else {
                        stock.setForwardPE(forwardPE);
                    }

                    if (marketCap.isNaN()) {
                        stock.setMarketCap(null);
                    } else {
                        stock.setMarketCap(marketCap);
                    }
                    stock.setFiftyTwoWeekLow(fiftyTwoWeekLow);
                    stock.setFiftyTwoWeekHigh(fiftyTwoWeekHigh);
                    stock.setFiftyDayAverage(fiftyDayAverage);
                    if (priceToSalesTrailing12Months.isNaN()) {
                        stock.setPriceToSalesTrailing12Months(null);
                    } else {
                        stock.setPriceToSalesTrailing12Months(priceToSalesTrailing12Months);
                    }
                    stock.setTwoHundredDayAverage(twoHundredDayAverage);
                }
            }

            if (targetInfo != null) {
                JSONObject targetDataObject = new JSONObject(targetInfo);
                JSONObject quoteSummaryTargetObject = targetDataObject.getJSONObject("quoteSummary");
                JSONArray targetResultArray = quoteSummaryTargetObject.getJSONArray("result");


                if (targetResultArray != null && !targetResultArray.isNull(0)) {
                    JSONObject targetFirstResult = targetResultArray.getJSONObject(0);
                    JSONObject financialData = targetFirstResult.getJSONObject("financialData");

                    // "financialData" 객체에 "targetHighPrice", "targetLowPrice", "targetMedianPrice" 키가 있는지 확인
                    if (financialData.has("targetHighPrice") && financialData.has("targetLowPrice") && financialData.has("targetMedianPrice")) {
                        stock.setCurrentPrice(financialData.getJSONObject("currentPrice").getDouble("raw"));

                        Double targetHighPrice = financialData.getJSONObject("targetHighPrice").optDouble("raw");
                        stock.setTargetHighPrice(Double.isNaN(targetHighPrice) ? null : targetHighPrice);

                        Double targetLowPrice = financialData.getJSONObject("targetLowPrice").optDouble("raw");
                        stock.setTargetLowPrice(Double.isNaN(targetLowPrice) ? null : targetLowPrice);

                        Double targetMedianPrice = financialData.getJSONObject("targetMedianPrice").optDouble("raw");
                        stock.setTargetMedianPrice(Double.isNaN(targetMedianPrice) ? null : targetMedianPrice);
                    }


                }
            }

            if (annInfo != null) {
                JSONObject stockDataObject = new JSONObject(annInfo);
                JSONObject quoteSummaryObject = stockDataObject.getJSONObject("quoteSummary");
                JSONArray resultArray = quoteSummaryObject.getJSONArray("result");

                if (resultArray != null && !resultArray.isNull(0)) {
                    JSONObject recommendationTrendObject = resultArray.getJSONObject(0).getJSONObject("recommendationTrend");
                    JSONArray trendArray = recommendationTrendObject.getJSONArray("trend");

                    JSONObject trendObject = trendArray.getJSONObject(0);
                    String period = trendObject.getString("period");

                    if (period.equals("0m")) {
                        long strongBuy = trendObject.getLong("strongBuy");
                        long buy = trendObject.getLong("buy");
                        long hold = trendObject.getLong("hold");
                        long sell = trendObject.getLong("sell");
                        long strongSell = trendObject.getLong("strongSell");

                        stock.setStrongBuy(strongBuy);
                        stock.setBuy(buy);
                        stock.setHold(hold);
                        stock.setSell(sell);
                        stock.setStrongSell(strongSell);

                    }
                }
            }

            return stock;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 스케줄러에서 매일 아침 7시5분에 업데이트 해주는 메서드
    public RsData<String> updateStock(String symbol, String companyInfo, String targetInfo, String annInfo) {
        Stock existingStock = stockRepository.findBySymbol(symbol);

        if (existingStock != null) {
            Stock updatedStock = yahooParseStockCompanyData(companyInfo, targetInfo, annInfo);

            if (updatedStock != null) {
                updatedStock.setId(existingStock.getId());  // 기존 주식 정보의 ID 설정
                updatedStock.setSymbol(symbol);  // 종목 심볼 설정
                stockRepository.save(updatedStock);
                return RsData.successOf("종목이 업데이트되었습니다.");
            } else {
                return RsData.failOf("종목 업데이트에 실패했습니다.");
            }
        } else {
            return RsData.failOf("존재하지 않는 종목입니다.");
        }
    }

    public Stock getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    public List<Stock> getStockList() {
        return stockRepository.findAll();
    }

}