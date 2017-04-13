package mbaas.com.nifty.advancepush;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private Common common;
    private static final String TAG = "InfoActivity";
    private static final int REQUEST_RESULT  = 0;

    TextView _nickname;
    TextView _prefecture;
    TextView _gender;
    TextView _email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        _nickname = (TextView) findViewById(R.id.txtNickname_Info);
        _prefecture = (TextView) findViewById(R.id.txtPrefecture_Info);
        _gender = (TextView) findViewById(R.id.txtGender_Info);
        _email = (TextView) findViewById(R.id.txtEmail_Info);

        // グローバル変数を扱うクラスを取得する
        common = (Common) getApplication();

        Log.d(TAG, common.currentUser.getString("nickname"));

        _nickname.setText(common.currentUser.getString("nickname"));
        _prefecture.setText(common.currentUser.getString("prefecture"));
        _gender.setText(common.currentUser.getString("gender"));
        _email.setText(common.currentUser.getMailAddress());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.onHomeInfoFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, REQUEST_RESULT );
            }
        });

    }


}
