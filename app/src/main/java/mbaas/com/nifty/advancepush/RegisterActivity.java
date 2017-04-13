package mbaas.com.nifty.advancepush;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Common common;
    private static final String TAG = "RegisterActivity";
    private static final int REQUEST_RESULT  = 0;

    EditText _nickname;
    EditText _prefecture;
    RadioGroup _groupGender;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // グローバル変数を扱うクラスを取得する
        common = (Common) getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        _nickname = (EditText) findViewById(R.id.txtNickname);
        _prefecture = (EditText) findViewById(R.id.txtPrefecture);
        _groupGender = (RadioGroup) findViewById(R.id.radioGroupGender);

        Button _registerButton = (Button) findViewById(R.id.btnRegisterUser);

        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //サインアップ画面遷移する
                doRegister();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    protected void doRegister() {

        String nickname = _nickname.getText().toString();
        String prefecture = _prefecture.getText().toString();
        Integer id = _groupGender.getCheckedRadioButtonId();
        String selectedGender = (String) ((RadioButton) findViewById(id)).getText();
        final List<String> list = new ArrayList<>();

        //**************** 【mBaaS/User③: ユーザー情報更新】***************
        common.currentUser.put("nickname", nickname);
        common.currentUser.put("prefecture", prefecture);
        common.currentUser.put("gender", selectedGender);
        common.currentUser.put("favorite",list);

        common.currentUser.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {
                    // 更新失敗時の処理
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("Save failed! Error:" + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    // 更新成功時の処理
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("保存成功しました! 入力ありがとうございます")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivityForResult(intent, REQUEST_RESULT );
                                }
                            })
                            .show();
                }
            }
        });

        //**************** 【mBaaS：プッシュ通知②】installationにユーザー情報を紐づける ***************
        NCMBInstallation currInstallation  = NCMBInstallation.getCurrentInstallation();
        currInstallation.put("prefecture", prefecture);
        currInstallation.put("gender", selectedGender);
        currInstallation.put("favorite", list);
        currInstallation.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {
                    //更新失敗時の処理
                    Log.d(TAG, "端末情報を保存失敗しました。");
                } else {
                    //更新成功時の処理
                    Log.d(TAG, "端末情報を保存成功しました。");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent, REQUEST_RESULT );
                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mbaas.com.nifty.advancepush/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mbaas.com.nifty.advancepush/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
