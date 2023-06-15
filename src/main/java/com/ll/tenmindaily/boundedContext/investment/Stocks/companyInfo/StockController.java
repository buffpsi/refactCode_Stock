package com.ll.tenmindaily.boundedContext.investment.Stocks.companyInfo;

import com.ll.tenmindaily.base.rsData.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/stocks")
public class StockController {

    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/add")
    public String addMain() {
        return "usr/Stock/StockAddHome";
    }

    @GetMapping("/")
    public String showMain() {
        return "usr/Stock/StockHome";
    }


    //야후 파이낸스 회사정보 볼 수 있도록
    @GetMapping("/yahoo/company/{symbol}")
    public ResponseEntity<String> getYahooCompanyInfo(@PathVariable String symbol) {
        try {
            String companyInfo = stockService.getYahooCompanyInfo(symbol);
            return ResponseEntity.ok(companyInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 야후 파이낸스 API를 통해 애널리스트들의 예측 정보를 가져오는 메서드
    @GetMapping("/yahoo/analystPredictions/{symbol}")
    public ResponseEntity<String> getYahooAnalystPredictions(@PathVariable String symbol) {
        try {
            String analystPredictions = stockService.getYahooAnalystPredictions(symbol);
            return ResponseEntity.ok(analystPredictions);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 야후 파이낸스 API를 통해 주식의 재무 데이터를 가져오는 메서드
    @GetMapping("/yahoo/financialData/{symbol}")
    public ResponseEntity<String> getYahooFinancialData(@PathVariable String symbol) {
        try {
            String financialData = stockService.getYahooFinancialData(symbol);
            return ResponseEntity.ok(financialData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //yahoo fiance api db 저장 메서드
    @PostMapping("/add/{symbol}")
    @ResponseBody
    public ResponseEntity<Integer> saveStockData(@PathVariable String symbol) {
        try {
            String companyInfo = stockService.getYahooCompanyInfo(symbol);
            String targetInfo = stockService.getYahooFinancialData(symbol);
            String annInfo = stockService.getYahooAnalystPredictions(symbol);

            RsData<Integer> result = stockService.saveStockCompanyData(symbol, companyInfo, targetInfo, annInfo);

            if (result.getData() == 1) {
                return ResponseEntity.ok(result.getData());
            } else if (result.getData() == 2) {
                return ResponseEntity.ok(result.getData());
            } else {
                return ResponseEntity.badRequest().body(result.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //실제로 DB에 있는 내용을 보여주는메서드
    @GetMapping("/show")
    public String showStockInfo(@RequestParam String symbol, Model model) {
        Stock stock = stockService.getStockBySymbol(symbol);
        if (stock != null) {
            model.addAttribute("stock", stock);
            return "usr/Stock/stockInfo";
        } else {
            return "usr/Stock/error";
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<Stock>> getStockList() {
        List<Stock> stockList = stockService.getStockList();
        return ResponseEntity.ok(stockList);
    }
}