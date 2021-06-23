package wo.test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import java.io.IOException;

import wo.main.Constants;

public class HttpStockCodeRequest {
    private static final String stockCodeUrl = "http://comp.fnguide.com/XML/Market/CompanyList.txt";

    public static void main(String args[]) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(Jsoup.connect(Constants.STOCK_CODE_URL).ignoreContentType(true).execute().parse().select(Constants.ALL_STOCK_TAG).text());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.ALL_STOCK_KEY);
        for(Object stock : jsonArray) {
            JSONObject obj = (JSONObject) stock;
            System.out.println(obj.get(Constants.STOCK_CODE_KEY) + " : " + obj.get(Constants.STOCK_NAME_KEY)) ;
        }
    }
}
