package wo.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.commexpert.CommExpertMng;
import com.truefriend.corelib.commexpert.intrf.IExpertInitListener;
import com.truefriend.corelib.commexpert.intrf.IExpertLoginListener;

public class HantooLoginActivity extends AppCompatActivity implements IExpertInitListener, IExpertLoginListener {

    private static boolean isInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isInit) {

            setContentView(R.layout.hantoo_login);

            EditText idText = findViewById(R.id.email);
            EditText pwText = findViewById(R.id.password);
            EditText certText = findViewById(R.id.cert);

            idText.setText(MainActivity.getgStrId());
            pwText.setText(MainActivity.getgStrPw());
            certText.setText(MainActivity.getgStrCert());

            CommExpertMng.InitActivity(this); // 메인 activity를 이 객체로 정함.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) // os 버전이 23 이상이면 수행
            {
                if (!com.openapi.sample.PermissionManager.getInstance().checkPermission()) { // PermissionManager.getInstance()의 결과물은 static PermissionManager 변수로 단 하나의 객체만이 생성되고 리턴된다.
                    com.openapi.sample.PermissionManager.getInstance().setOnPermissionListener(new com.openapi.sample.PermissionManager.OnPermissionListener() {
                        @Override
                        public void onPermissionResult(boolean isSucs, Object objPermission) // callback
                        {
                            if (isSucs) {
                                startApp();
                            } else {
                                Toast.makeText(getBaseContext(), "앱 권한 허용이 필요합니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }); // setOnPermissionListener는 GUI에서의 addEventListener랑 비슷한거같다. 즉, Permission.getInstance()에서 받아온 객체에 권한에 대한 이벤트가 발생하면 실행되는 메소드를 구현하는거라고 보면 되겠다.
                    com.openapi.sample.PermissionManager.getInstance().requestPermissions();
                } else { // 이미 권한 있음
                    startApp();
                }
            } else { // OS 버전이 23 이상이 아니면 수행, 즉 22 이하 구버전들은 권한에 대한 설정이 필요가 없는 모양
                startApp();
            }
        }
    }

    public void startApp()
    {
        /**
         * ExpertMng 기본 셋팅
         */
        // 초기화 및 통신 접속
        CommExpertMng.InitCommExpert(this);
        //Listener 셋팅
        CommExpertMng.getInstance().SetInitListener(this); // 인자 타입이 IExpertInitListener 인터페이스인데 sampleActivity 클래스에서 구현을 함.
        CommExpertMng.getInstance().SetLoginListener(this); // 위와 유사.
        //"0"리얼 ,  "1" 모의투자
        CommExpertMng.getInstance().SetDevSetting("0");
    }

    public void onDestroy()
    {
        super.onDestroy();

        com.openapi.sample.PermissionManager.getInstance().release();

        /**
         * ExpertMng 종료...
         */
        CommExpertMng.getInstance().Close();
    }

    public void onSessionConnecting() { // 1
        // TODO Auto-generated method stub
        //Toast.makeText(this, "서버 접속 시작.", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onSessionConnected(boolean isSuccess, String strErrorMsg) { // 2-1, 2-2
        // TODO 성공이 안됨
        //서버 성공
        if(isSuccess == true)
        {
            Toast.makeText(this, strErrorMsg, Toast.LENGTH_SHORT ).show();
        }
        else//서버 실패
        {
            Toast.makeText(this, strErrorMsg, Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onAppVersionState(boolean isDone) { // 3
        // TODO Auto-generated method stub
        //Toast.makeText(this, "라이브러리 버젼체크 완료.", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onMasterDownState(boolean isDone) { // 4
        // TODO Auto-generated method stub
        //Toast.makeText(this, "Master 파일 DownLoad...", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onMasterLoadState(boolean isDone) { // 5
        // TODO Auto-generated method stub
        //Toast.makeText(this, "Master 파일 Loading...", Toast.LENGTH_SHORT ).show();
    }
    @Override
    public void onInitFinished() { // 6
        // TODO Auto-generated method stub
        /**
         * 로그인 View를 올리고 클릭 이벤트 지정
         */

        Button loginButton = findViewById(R.id.email_sign_in_button);
        Button cancelButton = findViewById(R.id.cancel);


        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText idText = findViewById(R.id.email);
                EditText pwText = findViewById(R.id.password);
                EditText certText = findViewById(R.id.cert);

                if( idText.getText().toString().isEmpty() )
                {
                    Toast.makeText( getBaseContext(), "아이디를 확인하세요.", Toast.LENGTH_SHORT ).show();
                    return;
                }
                else if( pwText.getText().toString().isEmpty() )
                {
                    Toast.makeText( getBaseContext(), "비밀번호를 확인하세요.", Toast.LENGTH_SHORT ).show();
                    return;
                }
                else if( certText.getText().toString().isEmpty() )
                {
                    Toast.makeText( getBaseContext(), "공인인증 비밀번호를 확인하세요.", Toast.LENGTH_SHORT ).show();
                    return;
                }

                if (CommExpertMng.getInstance().GetMotu()) CommExpertMng.getInstance().StartLogin(idText.getText().toString(), pwText.getText().toString());
                else CommExpertMng.getInstance().StartLogin(idText.getText().toString(), pwText.getText().toString(), certText.getText().toString());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) // by Activity class
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) // by Activity class
    {
        com.openapi.sample.PermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onRequiredRefresh() { // 7
        // TODO Auto-generated method stub
        Toast.makeText( getBaseContext(), "재접속 되었습니다.", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onLoginResult(boolean isSuccess, String strErrorMsg) { // 1
        // TODO Auto-generated method stub
        /*
        if(isSuccess == true )
            Toast.makeText( getBaseContext(), "로그인 TR 성공", Toast.LENGTH_SHORT ).show();
        else
            Toast.makeText( getBaseContext(), strErrorMsg, Toast.LENGTH_SHORT ).show();

         */
    }

    @Override
    public void onAccListResult(boolean isSuccess, String strErrorMsg) { // 2
        // TODO Auto-generated method stub
        /*
        if(isSuccess == true )
            Toast.makeText( getBaseContext(), "계좌리스트 조회 TR 성공", Toast.LENGTH_SHORT ).show();
        else
            Toast.makeText( getBaseContext(), strErrorMsg, Toast.LENGTH_SHORT ).show();
         */
    }

    @Override
    public void onPublicCertResult(boolean isSuccess) { // 3
        // TODO Auto-generated method stub
        /*
        String strMsg;
        if(isSuccess == true )
            strMsg = "공인인증 검증 성공";
        else
            strMsg = "공인인증 검증 실패";
        Toast.makeText( getBaseContext(), strMsg, Toast.LENGTH_SHORT ).show();

         */
    }

    @Override
    public void onLoginFinished() { // 4
        // TODO Auto-generated method stub
        isInit = true;
        String strMsg = "로그인 성공";
        Toast.makeText( getBaseContext(), strMsg, Toast.LENGTH_SHORT ).show();
        if(MainActivity._MainActivity != null) {
            MainActivity._MainActivity.finish();
        }
        // text 셋팅
        EditText idText = findViewById(R.id.email);
        EditText pwText = findViewById(R.id.password);
        EditText certText = findViewById(R.id.cert);

        MainActivity.setgStrId(idText.getText().toString());
        MainActivity.setgStrPw(pwText.getText().toString());
        MainActivity.setgStrCert(certText.getText().toString());

        Intent i = new Intent(this,  MainActivity.class);
        startActivity(i);

    }

    public static boolean getisInit() {
        return isInit;
    }

}
