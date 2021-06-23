package wo.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import wo.main.Constants;


public class HttpStockInfoRequest {
    public static void main(String args[]) throws IOException {
//        String stockUrl = "http://m.stock.naver.com/item/main.nhn#/stocks/%s/total";      // Naver
        String stockUrl = "http://m.finance.daum.net/m/item/main.daum?code=%s";      // Daum
        String code = "005930";
        String requestUrl = String.format(stockUrl, code);

        Document doc = Jsoup.connect(requestUrl).get();
        System.out.println(doc.getElementsByAttributeValue("class", "price").text());
        System.out.println(doc.getElementsByAttributeValue("class", "rate_fluc").text());
        System.out.println(doc.getElementsByAttributeValue("class", "price_fluc").text());
        Elements stockElements01 = doc.getElementsByAttributeValue("class", "summary").select("dd");
        Elements stockElements02 = doc.getElementsByAttributeValue("class", "item_idx_info stUp").select("span");

        if(stockElements02.size() == 0) {
            stockElements02 = doc.getElementsByAttributeValue(Constants.TAG_CLASS, Constants.ATTR_02).select(Constants.TAG_SPAN);
            if(stockElements02.size() == 0) {
                stockElements02 = doc.getElementsByAttributeValue(Constants.TAG_CLASS, Constants.ATTR_03).select(Constants.TAG_SPAN);
            }
        }
//
//        System.out.println(requestUrl);
//        System.out.println(stockElements02);
        String sPrice = stockElements01.get(0).text();
        String hPrice = stockElements01.get(1).text();
        String lPrice = stockElements01.get(2).text();
        String pPrice = stockElements02.get(2).text();
        String vPrice = stockElements02.get(3).text();
        String vRate = stockElements02.get(4).text();
        String tradeAmount = stockElements01.get(3).text();

        System.out.println("sPrice : " +  sPrice);        // 시가
        System.out.println("hPrice : " +  hPrice);        // 고가
        System.out.println("lPrice : " +  lPrice);        // 저가
        System.out.println("pPrice : " +  pPrice);        // 현재가
        System.out.println("vPrice : " +  vPrice);        // 변동금액
        System.out.println("vRate : " +  vRate);          // 변동률

    }
}
