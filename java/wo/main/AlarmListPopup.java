package wo.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Iterator;
import java.util.Set;

import wo.stock.AlarmStockVO;

public class AlarmListPopup extends Activity {

    private static ListView alarmList;

    public static ListView getAlarmListView() {
        return alarmList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alarm_list_popup);

        OrderListViewAdapter alarmAdapter = new OrderListViewAdapter(R.layout.alarm_list_item);
        alarmList = findViewById(R.id.alarm_list);
        alarmList.setAdapter(alarmAdapter);

        if (Constants.ALARM_STOCK_LIST.size() != 0) {
            Set set = Constants.ALARM_STOCK_LIST.keySet();
            Iterator<Integer> iter = set.iterator();
            while(iter.hasNext()) {
                int groupPosition = iter.next();
                AlarmStockVO alarmStockVO = Constants.ALARM_STOCK_LIST.get(groupPosition);
                alarmAdapter.addItem(alarmStockVO.getName(), alarmStockVO.getHighBound(), alarmStockVO.getLowBound());
            }
        }

        Button alarmPopupCancel = findViewById(R.id.listCancelButton);

        alarmPopupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
