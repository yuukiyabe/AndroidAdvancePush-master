package mbaas.com.nifty.advancepush;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;
import com.nifty.cloud.mb.core.LoginCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBUser;

public class TopActivity extends AppCompatActivity {

    private  Common common;
    private static final String TAG = "TopActivity";
    private static final int REQUEST_RESULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NCMB.initialize(this.getApplicationContext(),"4ee55e68676dd422a8a83cb900ab6c98e7ca060951466c601ec4704ad1e835cb","25cbb178077848a97ffc050505f00d418b17bdc24f2c02f4e3ac9d92bb17086a");


        common =(Common) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);


        Button _tologinButton = (Button) findViewById(R.id.button);
        Button _tohomeButton = (Button) findViewById(R.id.button2);

        _tologinButton.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
               startActivityForResult(intent, REQUEST_RESULT);
           }
        });
        _tohomeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                doHome();
            }
        });
    }

    protected void doHome(){
        NCMBUser.loginWithAnonymousInBackground(new LoginCallback() {
            @Override
            public void done(NCMBUser user, NCMBException e) {
                if (e != null){
                    //エラー処理
                    new AlertDialog.Builder(TopActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("Login failed! Error:" + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                }else{
                    common.currentUser = NCMBUser.getCurrentUser();
                    AlertDialog show = new AlertDialog.Builder(TopActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("ログイン成功")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String nickname = common.currentUser.getString("nickname");
                                    if (nickname != null && !nickname.isEmpty() && !nickname.equals("null")) {
                                        //メイン画面遷移します
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivityForResult(intent, REQUEST_RESULT);
                                    }
                                }
                            })
                            .show();
                }
            }
        });
    }
}
