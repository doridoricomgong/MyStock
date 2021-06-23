package wo.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private CandleStickChart chart;
    private Intent intent;
    private float recentDate, lateDate;
    private float startPrice, endPrice, highPrice, lowPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candle_chart);

        chart = findViewById(R.id.chart);

        ArrayList<CandleEntry> entries = new ArrayList<>();

        intent = getIntent();
        for(int i = 0; i < 25; i++) {
            String[] strDatas = intent.getStringArrayExtra(String.valueOf(24-i));
            startPrice = Float.valueOf(strDatas[1]);
            endPrice = Float.valueOf(strDatas[2]);
            highPrice = Float.valueOf(strDatas[3]);
            lowPrice = Float.valueOf(strDatas[4]);
            entries.add(new CandleEntry(Float.valueOf(i), highPrice, lowPrice, startPrice, endPrice));
        }

        recentDate = Float.valueOf(intent.getStringArrayExtra("0")[0]);
        lateDate = Float.valueOf(intent.getStringArrayExtra("24")[0]);



        CandleDataSet dataSet = new CandleDataSet(entries, "주식 차트");

        // 심지 부분
        dataSet.setShadowColor(Color.LTGRAY);
        dataSet.setShadowWidth(1f);

        // 음봉 부분
        dataSet.setDecreasingColor(Color.BLUE);
        dataSet.setDecreasingPaintStyle(Paint.Style.FILL);

        // 양봉 부분
        dataSet.setIncreasingColor(Color.RED);
        dataSet.setIncreasingPaintStyle(Paint.Style.FILL);

        // 동일
        dataSet.setNeutralColor(Color.DKGRAY);
        dataSet.setDrawValues(false);
        dataSet.setHighLightColor(Color.TRANSPARENT);

        chart.getAxisLeft().setDrawAxisLine(true);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setTextColor(Color.TRANSPARENT);

        chart.getAxisRight().setEnabled(true);

        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setAvoidFirstLastClipping(true);
        chart.getXAxis().setEnabled(true);
        //chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getLegend().setEnabled(false);

        chart.setData(new CandleData(dataSet));
        chart.setDescription(new Description());
        chart.getDescription().setEnabled(true);
        chart.setHighlightPerDragEnabled(false);
        chart.requestDisallowInterceptTouchEvent(true);
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(false);
        chart.invalidate();

    }

    public class DayAxisValueFormatter extends ValueFormatter {
        private final CandleStickChart chart;
        public DayAxisValueFormatter(CandleStickChart chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value) {
            return "1";
        }
    }

}
