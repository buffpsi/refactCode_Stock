package com.ll.tenmindaily.boundedContext.investment.Stocks.companyInfo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStock is a Querydsl query type for Stock
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStock extends EntityPathBase<Stock> {

    private static final long serialVersionUID = -799181108L;

    public static final QStock stock = new QStock("stock");

    public final NumberPath<Long> buy = createNumber("buy", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Double> currentPrice = createNumber("currentPrice", Double.class);

    public final NumberPath<Double> dividendRate = createNumber("dividendRate", Double.class);

    public final NumberPath<Double> dividendYield = createNumber("dividendYield", Double.class);

    public final StringPath exDividendDate = createString("exDividendDate");

    public final NumberPath<Double> fiftyDayAverage = createNumber("fiftyDayAverage", Double.class);

    public final NumberPath<Double> fiftyTwoWeekHigh = createNumber("fiftyTwoWeekHigh", Double.class);

    public final NumberPath<Double> fiftyTwoWeekLow = createNumber("fiftyTwoWeekLow", Double.class);

    public final NumberPath<Double> forwardPE = createNumber("forwardPE", Double.class);

    public final NumberPath<Long> hold = createNumber("hold", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> marketCap = createNumber("marketCap", Double.class);

    public final NumberPath<Integer> national = createNumber("national", Integer.class);

    public final NumberPath<Double> previousClose = createNumber("previousClose", Double.class);

    public final NumberPath<Double> priceToSalesTrailing12Months = createNumber("priceToSalesTrailing12Months", Double.class);

    public final NumberPath<Long> sell = createNumber("sell", Long.class);

    public final NumberPath<Long> strongBuy = createNumber("strongBuy", Long.class);

    public final NumberPath<Long> strongSell = createNumber("strongSell", Long.class);

    public final StringPath symbol = createString("symbol");

    public final NumberPath<Double> targetHighPrice = createNumber("targetHighPrice", Double.class);

    public final NumberPath<Double> targetLowPrice = createNumber("targetLowPrice", Double.class);

    public final NumberPath<Double> targetMedianPrice = createNumber("targetMedianPrice", Double.class);

    public final NumberPath<Double> twoHundredDayAverage = createNumber("twoHundredDayAverage", Double.class);

    public QStock(String variable) {
        super(Stock.class, forVariable(variable));
    }

    public QStock(Path<? extends Stock> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStock(PathMetadata metadata) {
        super(Stock.class, metadata);
    }

}

