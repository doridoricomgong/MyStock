package wo.main;

import org.json.simple.JSONArray;

import java.util.HashMap;

import wo.stock.AlarmStockVO;

public class Constants {
    public static final String STOCK_CODE_URL = "http://comp.fnguide.com/XML/Market/CompanyList.txt";
    public static final String STOCK_INFO_URL = "https://finance.naver.com/item/sise.nhn?code=%s";      // Daum

    public static String HINT_ALL_STOCK = "ALL STOCK";
    public static String HINT_INTEREST = "INTEREST STOCK";

    public static final String ALL_STOCK_KEY = "Co";
    public static final String ALL_STOCK_TAG = "body";
    public static final String STOCK_CODE_KEY = "cd";
    public static final String STOCK_NAME_KEY = "nm";
    public static final String TAG_CLASS = "class";
    public static final String TAG_BLIND = "blind";
    public static final String TAG_DD = "dd";
    public static final String TAG_A = "a";
    public static final String TAG_SPAN = "span";

//    public static final String ATTR_01 = "item_idx_info stDn";
    public static final String ATTR_02 = "item_idx_info stUp";
    public static final String ATTR_03 = "item_idx_info stFt";
    public static final String ATTR_04 = "link_name";
    public static final String ATTR_05 = "price";
    public static final String ATTR_06 = "rate_fluc";
    public static final String ATTR_07 = "price_fluc";

    public static final String UP = "▲";
    public static final String DN = "▼";

    public static JSONArray ALL_STOCK_LIST = null;               // All stock list(name & code)

    public static HashMap<Integer, String> DETECT_STOCK_LIST = new HashMap<Integer, String>();      // <GroupPostion, StockCode>
    public static HashMap<Integer, String> INTEREST_STOCK_LIST = new HashMap<Integer, String>();
    public static HashMap<Integer, AlarmStockVO> ALARM_STOCK_LIST = new HashMap<Integer, AlarmStockVO>();

    public static final int STOCK_DETECT_TERM  = 4000;
}
