package wo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.commexpert.CommExpertMng;
import com.commexpert.ExpertRealProc;
import com.commexpert.ExpertTranProc;
import com.truefriend.corelib.commexpert.intrf.IRealDataListener;
import com.truefriend.corelib.commexpert.intrf.ITranDataListener;

public class HantooOrderActivity extends AppCompatActivity implements ITranDataListener, IRealDataListener {

    Context context = this;

    ExpertTranProc m_JangoTranProc = null;				//잔고 조회
    ExpertTranProc	m_OrderTranProc = null;				//주문
    ExpertTranProc	m_OrderListTranProc = null;			//주문내역 조회

    ExpertRealProc m_OrderRealProc = null;				//주문체결 실시간

    int		m_nJangoRqId = -1;								//잔고 TR ID
    int		m_nOrderRqId = -1;								//주문 TR ID
    int		m_nOrderListRqId = -1;							//주문내역 TR ID

    String m_strCode = "";										//종목 코드
    String m_strCode2 = "005930";							//종목 코드
    String m_strUserID ="";										// 로그인 ID
    String m_strAccountCode = "01";							//계좌 상품 코드
    String m_strCurTR = "";									// 조회 중인 Test TR


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        m_strCode = intent.getStringExtra("code");

        setContentView(R.layout.order_main);

        init(context);

        int nCount = CommExpertMng.getInstance().GetAccountSize();
        if (nCount > 0) {
            String strAcc = "";
            String strAccName = "";
            String strAccCode = "";
            for (int i = 0; i < nCount; i++) {
                strAcc = CommExpertMng.getInstance().GetAccountNo(i);                //계좌번호
                strAccName = CommExpertMng.getInstance().GetAccountName(i);        //계좌명
                strAccCode = CommExpertMng.getInstance().GetAccountCode(i);        //상품코드
                if (strAccCode.contains("01"))    //위탁계좌
                {

                    SetAccount(strAcc);					//계좌번호
                    SetAccountCode(strAccCode);
                    setCode(m_strCode);

                    CommExpertMng.getInstance().SetCurrAccountInfo(strAcc, strAccCode);

                    break;
                }
            }
        }


    }

    //계좌번호 셋팅
    public void SetAccount(String strAccount)
    {
        EditText view = (EditText)findViewById(R.id.acc_id);
        if(view != null)
        {
            view.setText(strAccount);
        }
    }

    //계좌 상품 코드 셋팅
    public void SetAccountCode(String strAccountCode)
    {
        m_strAccountCode = strAccountCode;
    }

    public void setCode(String strCode) {
        EditText view = (EditText)findViewById(R.id.eStockCode);
        if(view != null) {
            view.setText(strCode);
        }
    }

    public void init(Context context) {

        m_OrderTranProc = new ExpertTranProc(context);
        m_OrderTranProc.InitInstance(this);
        m_OrderTranProc.SetShowTrLog(true);

        m_JangoTranProc = new ExpertTranProc(context);
        m_JangoTranProc.InitInstance(this);
        m_JangoTranProc.SetShowTrLog(true);

        m_OrderListTranProc = new ExpertTranProc(context);
        m_OrderListTranProc.InitInstance(this);
        m_OrderListTranProc.SetShowTrLog(true);

        //실시간 초기화
        m_OrderRealProc = new ExpertRealProc(context);
        m_OrderRealProc.InitInstance(this);
        m_OrderRealProc.SetShowTrLog(true);

        Button buttonBuy = findViewById(R.id.bBuy);
        Button buttonSell = findViewById(R.id.bSell);
        Button buttonModify = findViewById(R.id.bModify);
        Button buttonCalcel = findViewById(R.id.bModify);
        Button buttonAccConnect = findViewById(R.id.acc_login);

        ImageView textOrderList = findViewById(R.id.order_list_update);
        ImageView textJangoList = findViewById(R.id.text_jango_update);


        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_OrderTranProc.ClearInblockData();
                //계좌번호
                EditText viewacc = (EditText)findViewById(R.id.acc_id);
                if(viewacc == null) return;
                m_OrderTranProc.SetSingleData(0, 0, viewacc.getText().toString());
                //상품코드
                m_OrderTranProc.SetSingleData(0, 1, m_strAccountCode);
                //비밀번호
                EditText viewPass = (EditText)findViewById(R.id.acc_pw);
                if(viewPass == null) return;
                String strPass = viewPass.getText().toString();
                if(strPass.isEmpty())
                {
                    Toast.makeText( HantooOrderActivity.this, String.format("비밀번호 입력하세요" ), Toast.LENGTH_SHORT ).show();
                    return ;
                }
                String strEncPass =  m_OrderTranProc.GetEncryptPassword(strPass);
                m_OrderTranProc.SetSingleData(0, 2, strEncPass);
                EditText Codeview = (EditText)findViewById(R.id.eStockCode);
                if(Codeview == null) return;
                m_strCode = Codeview.getText().toString();
                m_OrderTranProc.SetSingleData(0, 3, m_strCode);							//상품코드
                m_OrderTranProc.SetSingleData(0, 4, "00");									//주문구분  00:지정가
                //주문수량
                EditText viewQty = (EditText)findViewById(R.id.eStockCount);
                String strOrderQty = viewQty.getText().toString();
                m_OrderTranProc.SetSingleData(0, 5,strOrderQty);
                //주문가격
                EditText viewPrice =  (EditText)findViewById(R.id.ePrice);
                String strOrderPrice = viewPrice.getText().toString();
                m_OrderTranProc.SetSingleData(0, 6, strOrderPrice);
                m_OrderTranProc.SetSingleData(0, 7, " ");									//연락전화번호
                //축약서명
                m_OrderTranProc.SetCertType(1);
                //매수주문
                m_nOrderRqId = m_OrderTranProc.RequestData("scabo");
            }
        });

        buttonSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_OrderTranProc.ClearInblockData();
                //계좌번호
                EditText viewacc = (EditText)findViewById(R.id.acc_id);
                if(viewacc == null) return;
                m_OrderTranProc.SetSingleData(0, 0, viewacc.getText().toString());
                //상품코드
                m_OrderTranProc.SetSingleData(0, 1, m_strAccountCode);
                //비밀번호
                EditText viewPass = (EditText)findViewById(R.id.acc_pw);
                if(viewPass == null) return;
                String strPass = viewPass.getText().toString();
                if(strPass.isEmpty())
                {
                    Toast.makeText( HantooOrderActivity.this, String.format("비밀번호 입력하세요" ), Toast.LENGTH_SHORT ).show();
                    return ;
                }
                String strEncPass =  m_OrderTranProc.GetEncryptPassword(strPass);
                m_OrderTranProc.SetSingleData(0, 2, strEncPass);
                EditText Codeview = (EditText)findViewById(R.id.eStockCode);
                if(Codeview == null) return;
                m_strCode = Codeview.getText().toString();
                m_OrderTranProc.SetSingleData(0, 3, m_strCode);					//상품코드
                m_OrderTranProc.SetSingleData(0, 4, "01");							//매도유형
                m_OrderTranProc.SetSingleData(0, 5, "00");							//주문구분   00:지정가
                //주문수량
                EditText viewQty = (EditText)findViewById(R.id.eStockCount);
                String strOrderQty = viewQty.getText().toString();
                m_OrderTranProc.SetSingleData(0, 6, strOrderQty);
                //주문가격
                EditText viewPrice =  (EditText)findViewById(R.id.ePrice);
                String strOrderPrice = viewPrice.getText().toString();
                m_OrderTranProc.SetSingleData(0, 7,strOrderPrice);
                m_OrderTranProc.SetSingleData(0, 8, "2 ");							//연락전화번호
                //축약서명
                m_OrderTranProc.SetCertType(1);
                //매도주문
                m_nOrderRqId = m_OrderTranProc.RequestData("scaao");
            }
        });

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_OrderTranProc.ClearInblockData();
                //계좌번호
                EditText viewacc = (EditText)findViewById(R.id.acc_id);
                if(viewacc == null) return;
                m_OrderTranProc.SetSingleData(0, 0, viewacc.getText().toString());
                //상품코드
                m_OrderTranProc.SetSingleData(0, 1, m_strAccountCode);
                //비밀번호
                EditText viewPass = (EditText)findViewById(R.id.acc_pw);
                if(viewPass == null) return;
                String strPass = viewPass.getText().toString();
                if(strPass.isEmpty())
                {
                    Toast.makeText( HantooOrderActivity.this, String.format("비밀번호 입력하세요" ), Toast.LENGTH_SHORT ).show();
                    return ;
                }
                String strEncPass =  m_OrderTranProc.GetEncryptPassword(strPass);
                m_OrderTranProc.SetSingleData(0, 2, strEncPass);
                //한국거래소전송주문조직번호
                EditText viewNo = (EditText)findViewById(R.id.eNo);
                if(viewNo == null) return;
                String strNo = viewNo.getText().toString();
                m_OrderTranProc.SetSingleData(0, 3, strNo);
                //원주문번호
                EditText viewOrderNumber = (EditText)findViewById(R.id.eOrderNumber);
                if(viewOrderNumber == null) return;
                String strOrderNumber = viewOrderNumber.getText().toString();
                m_OrderTranProc.SetSingleData(0, 4, strOrderNumber);
                m_OrderTranProc.SetSingleData(0, 5, "00");							//주문구분
                m_OrderTranProc.SetSingleData(0, 6, "01");							//정정취소구분코드	01정정02취소
                //주문수량
                EditText viewQty = (EditText)findViewById(R.id.eStockCount);
                String strOrderQty = viewQty.getText().toString();
                m_OrderTranProc.SetSingleData(0, 7, strOrderQty);
                //주문단가
                EditText viewPrice =  (EditText)findViewById(R.id.ePrice);
                String strOrderPrice = viewPrice.getText().toString();
                m_OrderTranProc.SetSingleData(0, 8,strOrderPrice);
                m_OrderTranProc.SetSingleData(0, 9, "N");							//잔량전부주문여부
                m_OrderTranProc.SetSingleData(0, 10, "2 ");							//연락전화번호
                //축약서명
                m_OrderTranProc.SetCertType(1);
                //정정주문
                m_nOrderRqId = m_OrderTranProc.RequestData("smco");
            }
        });

        buttonCalcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_OrderTranProc.ClearInblockData();
                //계좌번호
                EditText viewacc = (EditText)findViewById(R.id.acc_id);
                if(viewacc == null) return;
                m_OrderTranProc.SetSingleData(0, 0, viewacc.getText().toString());
                //상품코드
                m_OrderTranProc.SetSingleData(0, 1, m_strAccountCode);
                //비밀번호
                EditText viewPass = (EditText)findViewById(R.id.acc_pw);
                if(viewPass == null) return;
                String strPass = viewPass.getText().toString();
                if(strPass.isEmpty())
                {
                    Toast.makeText( HantooOrderActivity.this, String.format("비밀번호 입력하세요" ), Toast.LENGTH_SHORT ).show();
                    return ;
                }
                String strEncPass =  m_OrderTranProc.GetEncryptPassword(strPass);
                m_OrderTranProc.SetSingleData(0, 2, strEncPass);
                //한국거래소전송주문조직번호
                EditText viewNo = (EditText)findViewById(R.id.eNo);
                if(viewNo == null) return;
                String strNo = viewNo.getText().toString();
                m_OrderTranProc.SetSingleData(0, 3, strNo);
                //원주문번호
                EditText viewOrderNumber = (EditText)findViewById(R.id.eOrderNumber);
                if(viewOrderNumber == null) return;
                String strOrderNumber = viewOrderNumber.getText().toString();
                m_OrderTranProc.SetSingleData(0, 4, strOrderNumber);
                m_OrderTranProc.SetSingleData(0, 5, "00");							//주문구분
                m_OrderTranProc.SetSingleData(0, 6, "02");							//정정취소구분코드 	01정정02취소
                //주문수량
                EditText viewQty = (EditText)findViewById(R.id.eStockCount);
                String strOrderQty = viewQty.getText().toString();
                m_OrderTranProc.SetSingleData(0, 7, strOrderQty);
                //주문단가
                EditText viewPrice =  (EditText)findViewById(R.id.ePrice);
                String strOrderPrice = viewPrice.getText().toString();
                m_OrderTranProc.SetSingleData(0, 8,strOrderPrice);
                m_OrderTranProc.SetSingleData(0, 9, "N");							//잔량전부주문여부
                m_OrderTranProc.SetSingleData(0, 10, "2 ");							//연락전화번호
                //축약서명
                m_OrderTranProc.SetCertType(1);
                //취소주문
                m_nOrderRqId = m_OrderTranProc.RequestData("smco");
            }
        });

        textOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_OrderListTranProc.ClearInblockData();

                //계좌번호
                EditText viewacc = (EditText)findViewById(R.id.acc_id);
                if(viewacc == null) return ;
                m_OrderListTranProc.SetSingleData(0, 0, viewacc.getText().toString());
                //상품코드
                m_OrderListTranProc.SetSingleData(0, 1, m_strAccountCode);
                //비밀번호
                EditText viewPass = (EditText)findViewById(R.id.acc_pw);
                if(viewPass == null) return ;
                String strPass = viewPass.getText().toString();
                if(strPass.isEmpty())
                {
                    Toast.makeText( HantooOrderActivity.this, String.format("비밀번호 입력하세요" ), Toast.LENGTH_SHORT ).show();
                    return ;
                }
                String strEncPass =  m_OrderListTranProc.GetEncryptPassword(strPass);
                m_OrderListTranProc.SetSingleData(0, 2, strEncPass);
                m_OrderListTranProc.SetSingleData(0, 3, " ");				//연속조회검색조건100
                m_OrderListTranProc.SetSingleData(0, 4, " ");				//연속조회키100
                m_OrderListTranProc.SetSingleData(0, 5, "0");				//조회구분1 0-주문순, 1-종목순

                m_nOrderListRqId = m_OrderListTranProc.RequestData("smcp");
            }
        });

        textJangoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_JangoTranProc.ClearInblockData();
                //계좌번호
                EditText viewacc = (EditText)findViewById(R.id.acc_id);
                if(viewacc == null) return;
                m_JangoTranProc.SetSingleData(0, 0, viewacc.getText().toString());
                //상품코드
                m_JangoTranProc.SetSingleData(0, 1, m_strAccountCode);
                //비밀번호
                EditText viewPass = (EditText)findViewById(R.id.acc_pw);
                if(viewPass == null) return;
                String strPass = viewPass.getText().toString();
                if(strPass.isEmpty())
                {
                    Toast.makeText( HantooOrderActivity.this, String.format("비밀번호 입력하세요" ), Toast.LENGTH_SHORT ).show();
                    return ;
                }
                String strEncPass =  m_JangoTranProc.GetEncryptPassword(strPass);
                m_JangoTranProc.SetSingleData(0, 2, strEncPass);
                m_JangoTranProc.SetSingleData(0, 3, "N");				//시간외 단일가여부
                m_JangoTranProc.SetSingleData(0, 4, "N");				//오프라인 여부
                m_JangoTranProc.SetSingleData(0, 5, "01");				//조회구분
                m_JangoTranProc.SetSingleData(0, 6, "01");				//단가구분
                m_JangoTranProc.SetSingleData(0, 7, "N");				//펀드결제분 포함여부
                m_JangoTranProc.SetSingleData(0, 8, "N");				//융자금액자동상환여부
                m_JangoTranProc.SetSingleData(0, 9, "00");				//처리구분
                m_JangoTranProc.SetSingleData(0,10, " ");				//연속조회검색조건
                m_JangoTranProc.SetSingleData(0, 11, " " );				//연속조회키

                m_nJangoRqId = m_JangoTranProc.RequestData("satps");
            }
        });

        buttonAccConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_OrderListTranProc.ClearInblockData();

                //계좌번호
                EditText viewacc = (EditText)findViewById(R.id.acc_id);
                if(viewacc == null) return ;
                m_OrderListTranProc.SetSingleData(0, 0, viewacc.getText().toString());
                //상품코드
                m_OrderListTranProc.SetSingleData(0, 1, m_strAccountCode);
                //비밀번호
                EditText viewPass = (EditText)findViewById(R.id.acc_pw);
                if(viewPass == null) return ;
                String strPass = viewPass.getText().toString();
                if(strPass.isEmpty())
                {
                    Toast.makeText( HantooOrderActivity.this, String.format("비밀번호 입력하세요" ), Toast.LENGTH_SHORT ).show();
                    return ;
                }
                String strEncPass =  m_OrderListTranProc.GetEncryptPassword(strPass);
                m_OrderListTranProc.SetSingleData(0, 2, strEncPass);
                m_OrderListTranProc.SetSingleData(0, 3, " ");				//연속조회검색조건100
                m_OrderListTranProc.SetSingleData(0, 4, " ");				//연속조회키100
                m_OrderListTranProc.SetSingleData(0, 5, "0");				//조회구분1 0-주문순, 1-종목순

                m_nOrderListRqId = m_OrderListTranProc.RequestData("smcp");
            }
        });


    }





    @Override
    public void onTranDataReceived(String sTranID, int nRqId) {
        // TODO Auto-generated method stub
        //조회 데이터 받아서 처리

        if(m_nOrderRqId == nRqId)			//주문
        {
            String stNo= m_OrderTranProc.GetSingleData(0,0);				//한국거래소전송주문조직번호
            String strOrederNo= m_OrderTranProc.GetSingleData(0,1);		//주문번호
            String strTime = m_OrderTranProc.GetSingleData(0,2);			//주문시각
            //매도/매수 주문
            if(sTranID.contains("scabo") || sTranID.contains("scaao"))
            {
                EditText viewNo = (EditText)findViewById(R.id.eNo);
                if(viewNo != null)
                    viewNo.setText(String.format("%s",stNo));

                EditText viewOrderNumber = (EditText)findViewById(R.id.eOrderNumber);
                if(viewOrderNumber != null)
                    viewOrderNumber.setText(String.format("%s",strOrederNo));
            }
            Log.d("==주식 주문==", String.format("한국거래소전송주문조직번호:%s 주문번호:%s 주문시각:%s",  stNo ,  strOrederNo  , strTime));
        }
        else if(m_nOrderListRqId == nRqId)			//주문 리스트
        {
            if( m_OrderListTranProc.m_sMsgText.contains("비밀번호") ) {
                return;
            }

            LinearLayout topLinearLayout = findViewById(R.id.toplinearLayout);
            topLinearLayout.setVisibility(View.GONE);

            m_JangoTranProc.ClearInblockData();
            //계좌번호
            EditText viewacc = (EditText)findViewById(R.id.acc_id);
            if(viewacc == null) return;
            m_JangoTranProc.SetSingleData(0, 0, viewacc.getText().toString());
            //상품코드
            m_JangoTranProc.SetSingleData(0, 1, m_strAccountCode);
            //비밀번호
            EditText viewPass = (EditText)findViewById(R.id.acc_pw);
            if(viewPass == null) return;
            String strPass = viewPass.getText().toString();
            if(strPass.isEmpty())
            {
                Toast.makeText( HantooOrderActivity.this, String.format("비밀번호 입력하세요" ), Toast.LENGTH_SHORT ).show();
                return ;
            }
            String strEncPass =  m_JangoTranProc.GetEncryptPassword(strPass);
            m_JangoTranProc.SetSingleData(0, 2, strEncPass);
            m_JangoTranProc.SetSingleData(0, 3, "N");				//시간외 단일가여부
            m_JangoTranProc.SetSingleData(0, 4, "N");				//오프라인 여부
            m_JangoTranProc.SetSingleData(0, 5, "01");				//조회구분
            m_JangoTranProc.SetSingleData(0, 6, "01");				//단가구분
            m_JangoTranProc.SetSingleData(0, 7, "N");				//펀드결제분 포함여부
            m_JangoTranProc.SetSingleData(0, 8, "N");				//융자금액자동상환여부
            m_JangoTranProc.SetSingleData(0, 9, "00");				//처리구분
            m_JangoTranProc.SetSingleData(0,10, " ");				//연속조회검색조건
            m_JangoTranProc.SetSingleData(0, 11, " " );				//연속조회키

            m_nJangoRqId = m_JangoTranProc.RequestData("satps");

            String strNo = " ", strOrderNumber = " ", strSellBuy =" ", strCode = " ", strName = " ";
            // Adapter 생성
            OrderListViewAdapter adapter = new OrderListViewAdapter(R.layout.order_list_item);

            // 리스트뷰 참조 및 Adapter 달기
            ListView listview = (ListView) findViewById(R.id.list_order);
            listview.setAdapter(adapter);

            int nCount = m_OrderListTranProc.GetValidCount(0);
            for(int i = 0; i < nCount; i++ )
            {

                strNo = m_OrderListTranProc.GetMultiData(0,0, i); 					//주문채번지점번호
                strOrderNumber = m_OrderListTranProc.GetMultiData(0,1,i);			//주문번호
                strSellBuy = m_OrderListTranProc.GetMultiData(0,3,i);					//주문구분명
                strCode= m_OrderListTranProc.GetMultiData(0,4,i);						//상품번호
                strName = m_OrderListTranProc.GetMultiData(0,6,i);					//정정취소구분명

                if(strOrderNumber.isEmpty())
                    continue;

                adapter.addItem(String.format("지점번호:%s 주문번호:%s 구분:%s  %s  %s", strNo, strOrderNumber, strSellBuy, strCode,strName ), null, null);
                //adapter.notifyDataSetChanged();
            }
        }
        else if(m_nJangoRqId == nRqId)		//잔고조회
        {
            //예수금 총금액
            String strTotal1 = m_JangoTranProc.GetMultiData(1, 0, 0);
            TextView viewTotal1 = (TextView)findViewById(R.id.text_total1);
            if(viewTotal1 != null)
                viewTotal1.setText(String.format("%d", Integer.parseInt( strTotal1 )));
            //총평가금액
            String strTotal2 = m_JangoTranProc.GetMultiData(1, 14, 0);
            TextView viewTotal2= (TextView)findViewById(R.id.text_total2);
            if(viewTotal2 != null)
                viewTotal2.setText(String.format("%d", Integer.parseInt( strTotal2 )));

            OrderListViewAdapter adapter = new OrderListViewAdapter(R.layout.order_list_item);

            // 리스트뷰 참조 및 Adapter 달기
            ListView listview = (ListView) findViewById(R.id.list_jango);
            listview.setAdapter(adapter);

            int nCount = m_JangoTranProc.GetValidCount(0);
            for(int i = 0;  i< nCount; i++)
            {
                //종목
                String strCode =  m_JangoTranProc.GetMultiData(0,1, i);
                //잔고
                String strQty = m_JangoTranProc.GetMultiData(0,7, i);


                if(!strCode.equals("")) {
                    adapter.addItem(strCode + " " + String.format("%d", Integer.parseInt(strQty)), null, null);
                }
            }
        }

    }
    @Override
    public void onTranMessageReceived(int nRqId, String strMsgCode,
                                      String strErrorType, String strMessage) {

        // TODO Auto-generated method stub
        Log.e("onTranMessageReceived", String.format("MsgCode:%s ErrorType:%s %s",  strMsgCode ,  strErrorType  , strMessage));
        Toast.makeText( HantooOrderActivity.this, String.format("%s", strMessage), Toast.LENGTH_SHORT ).show();


    }
    @Override
    public void onTranTimeout(int nRqId) {
        // TODO Auto-generated method stub

        Log.e("onTranTimeout", String.format("RqId:%d ",  nRqId));


    }
    @Override
    public void onRealDataReceived(String strServiceId) {

        if(strServiceId == "scn_r" || strServiceId == "scn_m")
        {
            String strOrderNumber = m_OrderRealProc.GetRealData(0,2);		//주문번호
            String strOrderGubun = m_OrderRealProc.GetRealData(0,4);			//매도매수구분
            String strCode = m_OrderRealProc.GetRealData(0,8);					//종목코드

            Log.d("==주식 체결통보==", String.format("주문번호:%s 매도매수구분:%s 종목코드:%s",  strOrderNumber ,  strOrderGubun  , strCode));
        }


    }
}
