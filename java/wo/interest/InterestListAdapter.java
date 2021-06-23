package wo.interest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import wo.main.Constants;
import wo.main.R;
import wo.stock.StockVO;

public class InterestListAdapter extends BaseAdapter {

    private ArrayList<InterestVO> interestOriginalList;
    private Context context;
    private ArrayList<InterestVO> interestList;

    public InterestListAdapter(Context context) {
        this.context = context;
        interestList = new ArrayList<InterestVO>();
        interestOriginalList = new ArrayList<InterestVO>();
    }

    @Override
    public int getCount() {
        return interestList.size();
    }

    @Override
    public Object getItem(int position) {
        return interestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final InterestVO interest = (InterestVO) getItem(position);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.interest_list_group, null);
        }
        TextView heading = view.findViewById(R.id.interest_list_title);
        TextView price = view.findViewById(R.id.interest_price);
        Button removeBtn = view.findViewById(R.id.interest_remove);
//        Button buyBtn = view.findViewById(R.id.interest_buy);
//        Button sellBtn = view.findViewById(R.id.interest_sell);

        removeBtn.setFocusable(false);
//        buyBtn.setFocusable(false);
//        sellBtn.setFocusable(false);

//        buyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, interest.getName() + " BUY", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        sellBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, interest.getName() + " SELL", Toast.LENGTH_SHORT).show();
//            }
//        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = interest.getName();
                interestList.remove(position);
                interestOriginalList.remove(position);
                Constants.INTEREST_STOCK_LIST.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, name + " removed", Toast.LENGTH_SHORT).show();
            }
        });
        heading.setText(interest.getName().trim());
//        price.setText(interest.getVariPrice() + " " + interest.getPrePrice() + " " + interest.getvRate());
        price.setText(interest.getvRate() + "\t\t" + interest.getVariPrice() + "원\t\t"  + interest.getPrePrice() +"원"  );

        return view;
    }

    public boolean addItem(InterestVO interestVO) { // 실질적으로 item이 추가되는 부분, 즉 여기서 쿼리를 보내버리자.
        for(InterestVO vo : interestList) { // 겹치는 이름있으면 false 리턴을 위한 반복문
            if(vo.getName().equals(interestVO.getName())) {
                return false;
            }
        }
        interestList.add(interestVO);
        interestOriginalList.add(interestVO);
        notifyDataSetChanged();
        return true;
    }

    public void filterData(String query) {
        Constants.DETECT_STOCK_LIST.clear();
        query = query.toLowerCase();
        interestList.clear();

        if (query.isEmpty()) {
            interestList.addAll(interestOriginalList);
        } else {
            for (InterestVO originalVO : interestOriginalList) {
                InterestVO newVO = new InterestVO();
                if (originalVO.getName().toLowerCase().contains(query)) {
                    newVO.setCode(originalVO.getCode());
                    newVO.setVariPrice(originalVO.getVariPrice());
                    newVO.setPrePrice(originalVO.getPrePrice());
                    newVO.setName(originalVO.getName());
                    newVO.setvRate(originalVO.getvRate());
                    interestList.add(newVO);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setStockVO(int groupPosition, StockVO stockVO) {
        if(interestList.get(groupPosition).getName().equals(stockVO.getName())) {
            InterestVO vo = interestList.get(groupPosition);
            vo.setvRate(stockVO.getvRate());
            vo.setVariPrice(stockVO.getVariPrice());
            vo.setPrePrice(stockVO.getPrePrice());
            vo.setCode(stockVO.getCode());
            vo.setName(stockVO.getName());
            notifyDataSetChanged();
        }
    }
}
