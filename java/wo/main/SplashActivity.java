package wo.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import wo.stock.AsyncAllStockListProcess;

/**
 * Created by cafe.naver.com/since201109 on 2018-02-17.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        new AsyncAllStockListProcess().execute();
        finish();

    }
}
