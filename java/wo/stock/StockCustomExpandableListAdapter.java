package wo.stock;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import wo.interest.InterestListAdapter;
import wo.interest.InterestVO;
import wo.main.Constants;
import wo.main.HantooLoginActivity;
import wo.main.HantooOrderActivity;
import wo.main.MainActivity;
import wo.main.PopupActivity;
import wo.main.R;

public class StockCustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;

    private ArrayList<StockGroupVO> stockGroupList;
    private ArrayList<StockGroupVO> stockOriginalList;

    private InterestListAdapter interestListAdapter;


    public StockCustomExpandableListAdapter(Context context, ArrayList<StockGroupVO> stockGroupList, InterestListAdapter interestListAdapter) {
        this.context = context;
        this.stockGroupList = new ArrayList<StockGroupVO>(); // deep copy를 위한 할당.
        this.stockGroupList.addAll(stockGroupList);
        this.stockOriginalList = new ArrayList<StockGroupVO>();
        this.stockOriginalList.addAll(stockGroupList);
        this.interestListAdapter = interestListAdapter;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return stockGroupList.get(groupPosition).getStockVO();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup parent) { // child에 대한 view를 만들어서 리턴해줌. 또한 view에 대한 주식 정보들은 이미 얻어진 상태여야함.
        StockVO stockVO = (StockVO) getChild(groupPosition, childPosition);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.stock_list_item, null);
        }

        TextView code = view.findViewById(R.id.stock_expanded_code);                        // 종목코드
        TextView sPriceView = view.findViewById(R.id.stock_expanded_start_price);          // 시가
        TextView hPriceView = view.findViewById(R.id.stock_expanded_high_price);           // 고가
        TextView lPriceView = view.findViewById(R.id.stock_expanded_low_price);            // 저가
        TextView pPriceView = view.findViewById(R.id.stock_expanded_pre_price);            // 현재가
        TextView vPriceView = view.findViewById(R.id.stock_expanded_v_price);              // 등락폭
        TextView rPriceView = view.findViewById(R.id.stock_expanded_rate_price);           // 등락률
        TextView tradeAmountView = view.findViewById(R.id.stock_expanded_trade_amount);    // 거래량


        code.setText(stockVO.getCode());

        sPriceView.setText(stockVO.getStartPrice());
        hPriceView.setText(stockVO.getHighPrice());
        lPriceView.setText(stockVO.getLowPrice());
        pPriceView.setText(stockVO.getPrePrice());
        vPriceView.setText(stockVO.getVariPrice());
        rPriceView.setText(stockVO.getvRate());
        tradeAmountView.setText(stockVO.getTradeAmount());


        sPriceView.startAnimation(animation);
        hPriceView.startAnimation(animation);
        lPriceView.startAnimation(animation);
        pPriceView.startAnimation(animation);
        vPriceView.startAnimation(animation);
        rPriceView.startAnimation(animation);
        tradeAmountView.startAnimation(animation);

        if(vPriceView.getText().toString().startsWith(Constants.UP)) {
            vPriceView.setTextColor(Color.RED);
            rPriceView.setTextColor(Color.RED);
        } else if(vPriceView.getText().toString().startsWith(Constants.DN)) {
            vPriceView.setTextColor(Color.BLUE);
            rPriceView.setTextColor(Color.BLUE);
        } else {
            vPriceView.setTextColor(Color.GRAY);
            rPriceView.setTextColor(Color.GRAY);
        }
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.stockGroupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.stockGroupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        final StockGroupVO stock = (StockGroupVO) getGroup(groupPosition);
        final String name = stock.getName();
        final String code = stock.getStockVO().getCode();
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.stock_list_group, null);
        }
        TextView heading = (TextView) view.findViewById(R.id.stock_list_title);
        Button addBtn = view.findViewById(R.id.stock_add);
        Button buyBtn = view.findViewById(R.id.stock_buy);
        Button alarmBtn = view.findViewById(R.id.stock_alarm);

        addBtn.setFocusable(false);
        buyBtn.setFocusable(false);
        alarmBtn.setFocusable(false);

        Button chartBtn = view.findViewById(R.id.stock_chart);
        chartBtn.setFocusable(false);

        chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HantooLoginActivity.getisInit()) {
                    MainActivity.m_TestTranProc.SetSingleData(0,0, "J");
                    MainActivity.m_TestTranProc.SetSingleData(0,1, code );
                    MainActivity.m_TestTranProc.SetSingleData(0,2, "D");
                    MainActivity.m_TestTranProc.SetSingleData(0,3, "1");

                    MainActivity.m_RqId = MainActivity.m_TestTranProc.RequestData("scpd");
                }
                else {
                    Toast.makeText(context, "로그인부터 해주십시오.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, HantooLoginActivity.class);
                    v.getContext().startActivity(intent);
                }
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HantooLoginActivity.getisInit()) {
                    Intent i = new Intent(context, HantooOrderActivity.class);
                    i.putExtra("code", code);
                    v.getContext().startActivity(i);
                }
                else {
                    Toast.makeText(context, "로그인부터 해주십시오.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, HantooLoginActivity.class);
                    v.getContext().startActivity(intent);
                }
            }
        });

        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 새로운 액티비티 팝업창 열기
                Intent intent = new Intent(context, PopupActivity.class);
                intent.putExtra("stockName", name);
                intent.putExtra("stockCode", code);
                intent.putExtra("stockGroupVO", groupPosition);

                v.getContext().startActivity(intent);


            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.INTEREST_STOCK_LIST.put(groupPosition, stock.getStockVO().getCode());
                InterestVO interestVO = new InterestVO();
                StockVO stockVO = stock.getStockVO();
                interestVO.setName(name);
                interestVO.setCode(stockVO.getCode());
                interestVO.setPrePrice(stockVO.getPrePrice());
                interestVO.setVariPrice(stockVO.getVariPrice());
                interestVO.setvRate(stockVO.getvRate());

                if(interestListAdapter.addItem(interestVO)) {
                    Toast.makeText(context, name + " added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, name + " already exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        heading.setText(name.trim());
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void filterData(String query) {
        Constants.DETECT_STOCK_LIST.clear();
        query = query.toLowerCase();
        stockGroupList.clear();

        if (query.isEmpty()) {
            stockGroupList.addAll(stockOriginalList);
        } else {
            for (StockGroupVO stock : stockOriginalList) {
                StockVO originalVO = stock.getStockVO();
                StockVO newVO = new StockVO();
                if (originalVO.getName().toLowerCase().contains(query)) {
                    newVO.setCode(originalVO.getCode());
                    newVO.setVariPrice(originalVO.getVariPrice());
                    newVO.setHighPrice(originalVO.getHighPrice());
                    newVO.setPrePrice(originalVO.getPrePrice());
                    newVO.setLowPrice(originalVO.getLowPrice());
                    newVO.setStartPrice(originalVO.getStartPrice());
                    newVO.setTradeAmount(originalVO.getTradeAmount());
                    newVO.setName(originalVO.getName());
                    newVO.setvRate(originalVO.getvRate());
                    StockGroupVO filterStock = new StockGroupVO(stock.getName(), newVO);
                    stockGroupList.add(filterStock);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setStockVO(int groupPosition, StockVO stockVO) {
        if(stockGroupList.get(groupPosition).getName().equals(stockVO.getName())) {
            stockGroupList.get(groupPosition).setStockVO(stockVO);
            notifyDataSetChanged();
        }
    }

    public ArrayList<StockGroupVO> getStockGroupList() {
        return stockGroupList;
    }

}