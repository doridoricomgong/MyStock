package wo.main;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import wo.interest.InterestListAdapter;
import wo.stock.AlarmStockVO;
import wo.stock.AsyncInterestListProcess;
import wo.stock.AsyncStockListProcess;
import wo.stock.StockCustomExpandableListAdapter;
import wo.stock.StockGroupVO;
import wo.stock.StockVO;

import com.commexpert.ExpertTranProc;
import com.truefriend.corelib.commexpert.intrf.ITranDataListener;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, ITranDataListener {

    public static Activity _MainActivity;

    private LinearLayout mainLayout = null;
    private LinearLayout stockLayout = null;
    private LinearLayout interestLayout = null;
    private LinearLayout accountLayout = null;
    private LinearLayout infoLayout = null;

    private View mainView = null;
    private View stockView = null;
    private View interestView = null;
    private View accountView = null;
    private View infoView = null;

    private HashMap<Integer, LinearLayout> layoutMap = null;

    private ListView stockListView;
    private ListView interestListView;

    private ExpandableListView stockExpandableListView;
    private SearchView stockSearch;
    private SearchView interestSearch;
    private StockCustomExpandableListAdapter stockExpandableListAdapter;
    private InterestListAdapter interestListAdapter;
    private ArrayList<StockGroupVO> stockExpandableListDetail;

    private static String gStrId;
    private static String gStrPw;
    private static String gStrCert;

    public static  ExpertTranProc m_TestTranProc = null;
    public static int m_RqId;

    Handler stockHandler = new Handler() { // ??????????????? ????????? ?????????, DETECT_STOCK_LIST??? ????????? Stock??? ?????? ????????? ????????? ?????? ???????????? ?????????.
        public void handleMessage(Message msg) {
            //t.setText("" + Constants.DETECT_STOCK_LIST.size());
            if (Constants.DETECT_STOCK_LIST.size() != 0) {
                Set set = Constants.DETECT_STOCK_LIST.keySet();
                Iterator<Integer> iter = set.iterator();
                while(iter.hasNext()) {
                    int groupPosition = iter.next();
                    String code = Constants.DETECT_STOCK_LIST.get(groupPosition);
                    try {
                        AsyncTask<Void, Void, StockVO> task = new AsyncStockListProcess(code).execute();
                        StockVO v = task.get(); // AsyncStockListProcess??? doInBackground??? ????????? ????????? StockVO??? ?????????.
                        stockExpandableListAdapter.setStockVO(groupPosition, v); // ????????? ?????? ????????? ????????? ???????????? ?????????.
                    } catch (Exception e) {
                    }
                }
            }

            if (Constants.ALARM_STOCK_LIST.size() != 0) {
                Set set = Constants.ALARM_STOCK_LIST.keySet();
                Iterator<Integer> iter = set.iterator();
                while(iter.hasNext()) {
                    int groupPosition = iter.next();
                    AlarmStockVO alarmStockVO = Constants.ALARM_STOCK_LIST.get(groupPosition);
                    String code = alarmStockVO.getCode();
                    try {
                        AsyncTask<Void, Void, StockVO> task = new AsyncStockListProcess(code).execute();
                        StockVO v = task.get(); // AsyncStockListProcess??? doInBackground??? ????????? ????????? StockVO??? ?????????.

                        int pPrice = Integer.valueOf(v.getPrePrice().replaceAll(",", "")); // ????????? ?????????
                        int highBound = Integer.valueOf(alarmStockVO.getHighBound().replaceAll(",", ""));
                        int lowBound = Integer.valueOf(alarmStockVO.getLowBound().replaceAll(",", ""));
                        if (highBound < pPrice) {
                            Toast.makeText( getBaseContext(), v.getName() + "(" + code + ") ????????? ????????? ??????!!", Toast.LENGTH_SHORT ).show();
                        }

                        if (pPrice < lowBound) {
                            Toast.makeText( getBaseContext(), v.getName() + "(" + code + ") ????????? ????????? ??????!!", Toast.LENGTH_SHORT ).show();
                        }

                    } catch (Exception e) {
                    }
                }
            }
            stockHandler.sendEmptyMessageDelayed(0, 5000);
        }
    };

    // TODO ??? ?????????--
    Handler interestHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (Constants.INTEREST_STOCK_LIST.size() != 0) {
                Set set = Constants.INTEREST_STOCK_LIST.keySet();
                Iterator<Integer> iter = set.iterator();
                while(iter.hasNext()) {
                    int groupPosition = iter.next();
                    String code = Constants.INTEREST_STOCK_LIST.get(groupPosition);
                    try {
                        AsyncTask<Void, Void, StockVO> task = new AsyncInterestListProcess(code).execute();
                        StockVO v = task.get();
                        interestListAdapter.setStockVO(groupPosition, v);
                    } catch (Exception e) {
                    }
                }
            }
            // ?????? ???????????? ???????????? ?????????. ??????????????? ????????? (5?????????)
            interestHandler.sendEmptyMessageDelayed(0, 5000);
        }
    };

    //private LineChart chart;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _MainActivity = this;
        setContentView(R.layout.activity_main);

        layoutMap = new HashMap<Integer, LinearLayout>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AlarmListPopup.class);

                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainLayout = findViewById(R.id.main); // Linear Layout
        //stockLayout = findViewById(R.id.stock); // Linear Layout
        interestLayout = findViewById(R.id.interest); // Linear Layout
        accountLayout = findViewById(R.id.account); // Linear Layout
        infoLayout = findViewById(R.id.info);

        layoutMap.put(R.id.menu_main, mainLayout); // onNavigationItemSelected ?????? ????????? layoutMap??? ?????????. menu_main??? ????????????, mainLayout??? visible?????? ?????? ??????.
        //layoutMap.put(R.id.menu_stock, stockLayout);
        layoutMap.put(R.id.menu_interest, interestLayout);
        layoutMap.put(R.id.menu_account, accountLayout);
        layoutMap.put(R.id.menu_info, infoLayout);

        mainView = LayoutInflater.from(this).inflate(R.layout.stock_main, null, false);
        //stockView = LayoutInflater.from(this).inflate(R.layout.stock_main, null, false); // mainView, stockView?????? ????????? ??????.
        interestView = LayoutInflater.from(this).inflate(R.layout.interest_main, null, false);
        accountView = LayoutInflater.from(this).inflate(R.layout.account_main, null, false);
        infoView = LayoutInflater.from(this).inflate(R.layout.info_main, null, false);

        mainLayout.addView(mainView); // view??? ????????? ????????? view?????? ????????? ??? ??????.
        //stockLayout.addView(stockView);
        interestLayout.addView(interestView);
        accountLayout.addView(accountView);
        infoLayout.addView(infoView);

        selectLayout(R.id.menu_main);

        //????????????, ???????????? ????????? ?????????
        stockListView = findViewById(R.id.stock_list_view);
        interestListView = findViewById(R.id.interest_list_view);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        stockSearch = findViewById(R.id.stock_search);
        stockSearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        stockSearch.setIconifiedByDefault(false);
        stockSearch.setOnQueryTextListener(this);
        stockSearch.setOnCloseListener(this);
        stockSearch.setQueryHint(Constants.HINT_ALL_STOCK);

        interestSearch = findViewById(R.id.interest_search);
        interestSearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        interestSearch.setIconifiedByDefault(false);
        interestSearch.setOnQueryTextListener(this);
        interestSearch.setOnCloseListener(this);
        interestSearch.setQueryHint(Constants.HINT_INTEREST);

        // Expandable ListView
        stockExpandableListDetail = new ArrayList<StockGroupVO>();
//        interestExpandableListDetail = new ArrayList<InterestGroupVO>();
        if (Constants.ALL_STOCK_LIST != null) { // [{"nm":"3S","gb":"701","cd":"A060310"}, ... ] // ????????? splash.java?????? ?????? stock??? ?????? ???????????? ?????????.
            String code = null;
            String name = null;
            int c = 0;
            for (Object stock : Constants.ALL_STOCK_LIST) {
                JSONObject obj = (JSONObject) stock; // {"nm":"3S","gb":"701","cd":"A060310"}
                StockVO stockVO = new StockVO();
                name = obj.get(Constants.STOCK_NAME_KEY).toString();
                code = obj.get(Constants.STOCK_CODE_KEY).toString().substring(1); // code?????? ????????? ????????? A??? ?????? ??????
                stockVO.setCode(code);
                stockVO.setName(name);
                StockGroupVO stockGroupVO = new StockGroupVO(name, stockVO);
                //stockGroupVO.setStockVO(stockVO);
                stockExpandableListDetail.add(stockGroupVO);
            }
        }

        stockExpandableListView = findViewById(R.id.stock_list_view);
        interestListView = findViewById(R.id.interest_list_view);

        interestListAdapter = new InterestListAdapter(this);
        interestListView.setAdapter(interestListAdapter);
        stockExpandableListAdapter = new StockCustomExpandableListAdapter(this, stockExpandableListDetail, interestListAdapter);
        stockExpandableListView.setAdapter(stockExpandableListAdapter);
        // Test??? TextView
        //t = findViewById(R.id.text);
        stockHandler.sendEmptyMessageDelayed(0, Constants.STOCK_DETECT_TERM);
        interestHandler.sendEmptyMessageDelayed(0, Constants.STOCK_DETECT_TERM);

        m_TestTranProc = new ExpertTranProc(this);
        m_TestTranProc.InitInstance(this);
        m_TestTranProc.SetShowTrLog(true);


        // ?????? ???????????? ????????? ?????????, stockGroupList (StockCustomExpandableListAdapter.java) ??? ?????? ????????? ??????. ?????????, ????????? ????????? ??????????????? Handler?????? ??????????????? ??????????????? ??????.(?????????????????? ???????????? ????????????.)
        stockExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() { // ??????(??????)??? ?????? ??? ????????? ??? ?????? ????????? ??????.
            @Override
            public void onGroupExpand(int groupPosition) { // groupPosition??? ?????? ??????(??????)??? ????????????????????? ?????????. ?????? 3S ?????? ??? groupPosition??? 0???.
                collapseAll();
                String code = stockExpandableListAdapter.getStockGroupList().get(groupPosition).getStockVO().getCode(); // ??????????????? AsyncStockListProcess??? ???????????? ???????????????, ????????? stock ????????? ?????? X
                Constants.DETECT_STOCK_LIST.put(groupPosition, code);

            }
        });

        stockExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                if(Constants.DETECT_STOCK_LIST.containsKey(groupPosition)) {
                    Constants.DETECT_STOCK_LIST.remove(groupPosition);
                }
            }
        });

        stockExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        /*
        chart = (LineChart) findViewById(R.id.chart);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setTextColor(Color.WHITE);
        chart.animateXY(2000, 2000);
        chart.invalidate();

        LineData data = new LineData();
        chart.setData(data);

        feedMultiple();
         */
    }

    public void onTranDataReceived(String sTranID, int nRqId) {
        // TODO Auto-generated method stub
        //?????? ????????? ????????? ??????
        if(m_RqId == nRqId)
        {
            ProcessTRTest(sTranID);
        }
    }

    @Override
    public void onTranMessageReceived(int nRqId, String strMsgCode,
                                      String strErrorType, String strMessage) {

        // TODO Auto-generated method stub
        Log.e("onTranMessageReceived", String.format("MsgCode:%s ErrorType:%s %s",  strMsgCode ,  strErrorType  , strMessage));
        //Toast.makeText( HantooOrderActivity.this, String.format("%s", strMessage), Toast.LENGTH_SHORT ).show();


    }
    @Override
    public void onTranTimeout(int nRqId) {
        // TODO Auto-generated method stub

        Log.e("onTranTimeout", String.format("RqId:%d ",  nRqId));


    }

    public void ProcessTRTest(String strTR)
    {
        if(strTR.contains("scpd"))		//?????? ????????? ?????????
        {
            String strTime = " " ,strStartPrice = "0", strEndPrice = "0", strHighPrice = "0", strLowPrice = "0", strRate = "0";
            int nPriceAttr = -1;
            int nCount =m_TestTranProc.GetValidCount(0);
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            System.out.println("count is " + nCount);
            for(int i = 0; i < nCount; i++ )
            {
                strTime = m_TestTranProc.GetMultiData(0,0, i); 		//??????????????????
                strStartPrice = m_TestTranProc.GetMultiData(0,1,i);			//?????? ??????
                //nPriceAttr = m_TestTranProc.GetAttrMultiData(0,1,i);	//?????? ??????
                strEndPrice = m_TestTranProc.GetMultiData(0,4,i);    //??????
                strHighPrice = m_TestTranProc.GetMultiData(0,2,i);   //?????????
                strLowPrice = m_TestTranProc.GetMultiData(0,3,i);    //?????????
                
                
                //strRate = m_TestTranProc.GetMultiData(0,13,i);			//??????????????????

                Log.d("scpd:?????? ????????? ?????????", String.format("??????:%s ??????:%s(Attr:%d)  ??????????????????:%s", strTime, Integer.parseInt( strStartPrice ), nPriceAttr, Double.parseDouble(strRate) ));

                intent.putExtra(String.valueOf(i), new String[]{strTime, strStartPrice, strEndPrice, strHighPrice, strLowPrice});
            }
            MainActivity.this.startActivity(intent);
            //Toast.makeText( getBaseContext(),String.format("scpd:?????? ????????? ?????? : ??????:%s ??????:%s(Attr:%d)  ??????????????????:%s", strTime, Integer.parseInt( strStartPrice ), nPriceAttr, Double.parseDouble(strRate) ), Toast.LENGTH_SHORT ).show();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getBaseContext(), "Setting null", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { // OnNavigationItemSelectedListener 1
        // Handle navigation view item clicks here.
        selectLayout(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectLayout(int visibleMenu) {
        Iterator<Integer> iter = layoutMap.keySet().iterator();
        while (iter.hasNext()) {
            layoutMap.get(iter.next()).setVisibility(View.GONE);
        }
        layoutMap.get(visibleMenu).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onClose() { // OnCloseListener 1
        stockExpandableListAdapter.filterData("");
        interestListAdapter.filterData("");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) { // SearchView.OnQueryTextListener 1
        collapseAll();
        stockExpandableListAdapter.filterData(query);
        interestListAdapter.filterData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) { // SearchView.OnQueryTextListener 2
        collapseAll();
        stockExpandableListAdapter.filterData(query);
        interestListAdapter.filterData(query);
        return false;
    }

    private void collapseAll() {
        if(Constants.DETECT_STOCK_LIST.size() != 0) {
            Iterator<Integer> iter = Constants.DETECT_STOCK_LIST.keySet().iterator();
            while(iter.hasNext()) {
                stockExpandableListView.collapseGroup(iter.next());
            }
        }
    }
    /*
    private void addEntry() {
        LineData data = chart.getData();
        if(data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if(set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);

            data.notifyDataChanged();

            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(10);
            chart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setFillAlpha(110);
        set.setFillColor(Color.parseColor("#d7e7fa"));
        set.setColor(Color.parseColor("#0b80c9"));
        set.setCircleColor(Color.parseColor("#ffa1b4dc"));
        set.setCircleColorHole(Color.BLUE);
        set.setValueTextColor(Color.WHITE);
        set.setDrawValues(false);
        set.setLineWidth(2);
        set.setCircleRadius(6);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setHighLightColor(Color.rgb(244, 117, 117));

        return set;
    }

    private void feedMultiple() {
        if (thread != null) {
            thread.interrupt();
        }

        final Runnable runnable = new Runnable() {
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    runOnUiThread(runnable);
                    try {
                        Thread.sleep(3000);
                    } catch(InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
     */
    protected void onPause() {
        super.onPause();
        if (thread != null) {
            thread.interrupt();
        }
    }

    public static void setgStrId(String str) {
        gStrId = str;
    }

    public static void setgStrPw(String str) {
        gStrPw = str;
    }

    public static void setgStrCert(String str) {
        gStrCert = str;
    }

    public static String getgStrId() {
        return gStrId;
    }

    public static String getgStrPw() {
        return gStrPw;
    }

    public static String getgStrCert() {
        return gStrCert;
    }
}