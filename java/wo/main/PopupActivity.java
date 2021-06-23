package wo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import wo.stock.AlarmStockVO;

public class PopupActivity extends AppCompatActivity {

    private TextView stockNameTextView;
    private TextView stockCodeTextView;
    private TextView stockHighBoundTextView;
    private TextView stockLowBoundTextView;

    private String cPrice;
    private String highBound;
    private String lowBound;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alarm_popup);

        Intent intent = getIntent();
        final String stockName = intent.getStringExtra("stockName");
        final String stockCode = intent.getStringExtra("stockCode");
        final int groupPosition = intent.getIntExtra("stockGroupVO", -1);

        stockNameTextView = findViewById(R.id.alarm_stock_name);
        stockCodeTextView = findViewById(R.id.alarm_stock_code);
        stockHighBoundTextView = findViewById(R.id.alarm_high_rate);
        stockLowBoundTextView = findViewById(R.id.alarm_low_rate);

        stockNameTextView.setText(stockName);
        stockCodeTextView.setText(stockCode);


        Button setButton = findViewById(R.id.setButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                highBound = stockHighBoundTextView.getText().toString();
                lowBound = stockLowBoundTextView.getText().toString();

                AlarmStockVO vo = new AlarmStockVO(stockName, stockCode, highBound, lowBound);
                if( highBound.isEmpty() )
                {
                    Toast.makeText( getBaseContext(), "상한을 확인하세요.", Toast.LENGTH_SHORT ).show();
                    return;
                }
                else if( lowBound.isEmpty() )
                {
                    Toast.makeText( getBaseContext(), "하한을 확인하세요.", Toast.LENGTH_SHORT ).show();
                    return;
                }
                Constants.ALARM_STOCK_LIST.put(groupPosition, vo);
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
