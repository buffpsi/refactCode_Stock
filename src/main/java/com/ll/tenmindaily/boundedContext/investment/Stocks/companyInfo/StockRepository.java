package com.ll.tenmindaily.boundedContext.investment.Stocks.companyInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findBySymbol(String symbol);
}