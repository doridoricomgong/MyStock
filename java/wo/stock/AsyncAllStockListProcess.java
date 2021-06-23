package wo.stock;

import android.os.AsyncTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import java.io.IOException;

import wo.main.Constants;

public class AsyncAllStockListProcess extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(Jsoup.connect(Constants.STOCK_CODE_URL).ignoreContentType(true).execute().parse().select(Constants.ALL_STOCK_TAG).text());
            System.out.println(jsonObject);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constants.ALL_STOCK_LIST = (JSONArray) jsonObject.get(Constants.ALL_STOCK_KEY);
        System.out.println(Constants.ALL_STOCK_LIST);
        return null;
    }
}