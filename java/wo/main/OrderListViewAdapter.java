package wo.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import wo.stock.AlarmStockVO;

public class OrderListViewAdapter extends BaseAdapter {

    private TextView contentTextView;
    private TextView highRateTextView;
    private TextView lowRateTextView;
    private int layoutId;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<OrderListViewItem> listViewItemList = new ArrayList<OrderListViewItem>();

    // 생성자
    public OrderListViewAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        contentTextView = (TextView) convertView.findViewById(R.id.content);
        highRateTextView = (TextView) convertView.findViewById(R.id.text_high_rate);
        lowRateTextView = (TextView) convertView.findViewById(R.id.text_low_rate);

        OrderListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        final String[] contents = listViewItem.getContent();
        contentTextView.setText(contents[0]);
        highRateTextView.setText(contents[1]);
        lowRateTextView.setText(contents[2]);

        Button alarmRemoveButton = convertView.findViewById(R.id.alarm_remove);

        alarmRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((OrderListViewItem) getItem(position)).getContent()[0];
                Constants.ALARM_STOCK_LIST.remove(getKey(Constants.ALARM_STOCK_LIST, name));
                Toast.makeText(context, name + " removed", Toast.LENGTH_SHORT).show();
                listViewItemList.remove(position);
                notifyDataSetChanged();
            }
        });


        return convertView;
    }


    public static Integer getKey(Map<Integer, AlarmStockVO> map, String name) {
        for(Integer key : map.keySet()) {
            if(name.equals(map.get(key).getName())) {
                return key;
            }
        }
        return null;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수.
    public void addItem(String content, String highRate, String lowRate) {
        OrderListViewItem item = new OrderListViewItem();

        item.setContent(content, highRate, lowRate);

        listViewItemList.add(item);
    }
}