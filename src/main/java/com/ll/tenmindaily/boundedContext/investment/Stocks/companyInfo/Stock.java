package com.ll.tenmindaily.boundedContext.investment.Stocks.companyInfo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString(callSuper = true)
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    //한국 미국 기업명
//    private String name;
    //스케줄러의 기준 시간이 된다.
    @CreatedDate
    private LocalDateTime createDate;
    //티커
    private String symbol;
    //한국 시장 종목은 0, 미국시장일때는 1
    private Integer national;
    //장 종가
    private Double previousClose;
    //주당 배당금
    private Double dividendRate;
    //주가의 배당 비율
    private Double dividendYield;
    //배당권 분리일 즉 이 시간 이전에는 가지고 있어한다.
    private String exDividendDate;
    //시총
    private Double marketCap;
    //per
    private Double forwardPE;
    //52주 최저가
    private Double fiftyTwoWeekLow;
    //52주 최고가
    private Double fiftyTwoWeekHigh;
    //52주 평균가
    private Double fiftyDayAverage;
    //최근 12개월 매출액대비 주가 비율
    private Double priceToSalesTrailing12Months;
    //200일 이동 평균 주가
    private Double twoHundredDayAverage;

    //현재 종가
    private Double currentPrice;
    //타겟 하이
    private Double targetHighPrice;
    //타겟 로우
    private Double targetLowPrice;
    // 타켓 중위값
    private Double targetMedianPrice;
    //strongBuy
    private Long strongBuy;
    //Buy
    private Long buy;
    //Hold
    private Long hold;
    //Sell
    private Long sell;
    //Strong Sell
    private Long strongSell;
}


