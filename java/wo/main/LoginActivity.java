package wo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by cafe.naver.com/since201109 on 2018-02-18.
 */

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
    }

    public void guestLoginClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void hantooLoginClick(View view) {
        Intent i = new Intent(this, HantooLoginActivity.class);
        startActivity(i);
        finish();
    }
}