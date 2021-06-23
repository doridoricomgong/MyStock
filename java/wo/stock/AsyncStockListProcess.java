package wo.stock;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import wo.main.Constants;

public class AsyncStockListProcess extends AsyncTask<Void, Void, StockVO> {
    private String code;
    public AsyncStockListProcess(String code) {
        this.code = code;
    }

    @Override
    protected StockVO doInBackground(Void... voids) {
        String requestUrl = String.format(Constants.STOCK_INFO_URL, code);
        StockVO stockVO = new StockVO();
        String start = "test",high = "test", low = "test", trade = "test", pre = "test", vari = "test", rate = "test";
        Document doc = null;
        try {
            doc = Jsoup.connect(requestUrl).get();      // TODO Caused by: org.jsoup.UncheckedIOException: java.net.ProtocolException: unexpected end of stream
        } catch (Exception e) {
            Log.e("pdReport", e.getMessage());
        }


        Elements stockElements01 = doc.getElementsByAttributeValue(Constants.TAG_CLASS, Constants.TAG_BLIND).select(Constants.TAG_DD);

        String name = stockElements01.get(1).text().split(" ",2)[1]; // 스페이스 기준으로 인덱스 1로 하기.
        Element startElement = stockElements01.get(5); // 시가
        Element highElement = stockElements01.get(6); // 고가
        Element lowElement = stockElements01.get(8); // 저가
        Element tradeElement = stockElements01.get(10); // 거래량
        Element preElement = stockElements01.get(3); // 현재가, 등락률, 등락폭


        if(startElement.childNodeSize() != 0) start = startElement.text().split(" ")[1];
        if(highElement.childNodeSize() != 0) high = highElement.text().split(" ")[1];
        if(lowElement.childNodeSize() != 0) low = lowElement.text().split(" ")[1];
        if(tradeElement.childNodeSize() != 0) trade = tradeElement.text().split(" ")[1];
        if(preElement.childNodeSize() != 0) {
            pre = preElement.text().split(" ")[1];
            if( (preElement.text().split(" ")[3]).equals("상승") ) {
                vari = "▲" + preElement.text().split(" ")[4];
                rate = "▲" + preElement.text().split(" ")[6];
            }
            else if( (preElement.text().split(" ")[3]).equals("보합") ) {
                vari = "0";
                rate = "0.00";
            }
            else {
                vari = "▼" + preElement.text().split(" ")[4];
                rate = "▼" + preElement.text().split(" ")[6];
            }
        }


        stockVO.setCode(code);
        stockVO.setName(name);
        stockVO.setStartPrice(start);
        stockVO.setHighPrice(high);
        stockVO.setLowPrice(low);
        stockVO.setTradeAmount(trade);
        stockVO.setPrePrice(pre);
        stockVO.setVariPrice(vari);
        stockVO.setvRate(rate);

        return stockVO;
    }
}