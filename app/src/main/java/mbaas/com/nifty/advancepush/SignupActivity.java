package mbaas.com.nifty.advancepush;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBUser;

import butterknife.ButterKnife;
import butterknife.Bind;


public class SignupActivity extends AppCompatActivity {

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_frigana) EditText _friganaText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    private Common common;
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_RESULT = 0;
    EditText _signupEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // グローバル変数を扱うクラスを取得する
        common = (Common) getApplication();

        // グローバル変数を扱うクラスを初期化する
        common.init();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ButterKnife.bind(this);

       /* _signupEmail = (EditText) findViewById(R.id.txtSignupEmail);

        Button _backButton_onsignup = (Button) findViewById(R.id.btnBack_onsignup);
        Button _btnSignupByEmail = (Button) findViewById(R.id.btnSignupByEmail);

        _btnSignupByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
               // doSignupByEmail();
            }
        });

        _backButton_onsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });*/
    }

    public void signup(){
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("新規登録中");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String mailaddress = _emailText.getText().toString();
        String frigana = _friganaText.getText().toString();
        String password = _passwordText.getText().toString();

        //TODO: Implement your own signup logic here.
        //NCMBUserのインスタンスを作成
        NCMBUser user = new NCMBUser();
        //ユーザ名を設定
        user.setUserName(name);
        //パスワードを設定
        user.setPassword(password);
        //設定したユーザ名とパスワードで会員登録を行う
        user.setMailAddress(mailaddress);
        common.currentUser.put("frigana", frigana);
    }

    public void onSignupSuccess(){
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(){
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate(){
        boolean valid = true;

        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();
        String frigana = _friganaText.getText().toString();
        String email = _emailText.getText().toString();

        if (name.isEmpty() || name.length() < 3){
            _nameText.setError("3文字以上入力してください");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10){
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (frigana.isEmpty()){
            _friganaText.setError("ふりがなを入力してください");
            valid = false;
        } else {
            _friganaText.setError(null);
        }

        if (email.isEmpty()){
            _emailText.setError("メールアドレスを入力してください");
            valid =false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }

/*
    protected void doSignupByEmail() {
        //**************** 【mBaaS/User①】: 会員登録用メールを要求する】***************
        String email = _signupEmail.getText().toString();
        NCMBUser.requestAuthenticationMailInBackground(email, new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {
                    // 会員登録用メールの要求失敗時の処理
                    new AlertDialog.Builder(SignupActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("Send failed! Error:" + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    // 会員登録用メールの要求失敗時の処理
                    new AlertDialog.Builder(SignupActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("メール送信完了しました! メールをご確認ください。")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Login画面遷移します
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivityForResult(intent, REQUEST_RESULT);
                                }
                            })
                            .show();

                }
            }
        });
    }*/
}
