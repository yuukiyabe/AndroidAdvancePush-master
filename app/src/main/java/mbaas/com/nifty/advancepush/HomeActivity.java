package mbaas.com.nifty.advancepush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_RESULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button noticeButton = (Button)findViewById(R.id.button8);
        Button couponButton = (Button)findViewById(R.id.button9);
        Button infoButton = (Button)findViewById(R.id.button10);
        Button reserveButton = (Button)findViewById(R.id.button11);

        noticeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 画面遷移する　お知らせ
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });
        couponButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // クーポン
                Intent intent = new Intent(getApplicationContext(), CouponActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
                // startActivity(new Intent(Intent.ACTION_INSTALL_PACKAGE));
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // お店情報
                Intent intent = new Intent(getApplicationContext(), BasicinfoActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });
        reserveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 予約
                Intent intent = new Intent(getApplicationContext(), ReserveActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });
    }
}
