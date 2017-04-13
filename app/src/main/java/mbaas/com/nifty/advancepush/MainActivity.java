package mbaas.com.nifty.advancepush;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBPush;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_RESULT  = 0;
    private Common common;
    ListView lstShop;
    private ArrayAdapter<String> listAdapter;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //**************** 【mBaaS/Initialization: APIキーを指定する】***************
        NCMB.initialize(this.getApplicationContext(),"4ee55e68676dd422a8a83cb900ab6c98e7ca060951466c601ec4704ad1e835cb","25cbb178077848a97ffc050505f00d418b17bdc24f2c02f4e3ac9d92bb17086a");

        //**************** 【mBaaS/Push①: 端末を登録】***************
        //端末情報を扱うNCMBInstallationのインスタンスを作成する
        final NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();

        //GCMからRegistrationIdを取得しinstallationに設定する
        installation.getRegistrationIdInBackground("238148672451", new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    installation.saveInBackground(new DoneCallback() {
                        @Override
                        public void done(NCMBException e) {
                            if(e == null){
                                //保存成功
                                Log.d(TAG, "端末情報を保存成功しました。");
                            }else if(NCMBException.DUPLICATE_VALUE.equals(e.getCode())){
                                //保存失敗 : registrationID重複
                                updateInstallation(installation);
                            }else {
                                //保存失敗 : その他
                                Log.d(TAG, "端末情報を保存失敗しました。");
                            }
                        }
                    });
                } else {
                    //ID取得失敗
                }
            }
        });

        // グローバル変数を扱うクラスを取得する
        common = (Common) getApplication();
        if (common.currentUser != null) {

            lstShop = (ListView) findViewById(R.id.lstShop);

            setContentView(R.layout.activity_main);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.onFavoriteFab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // お気に入り画面遷移する
                    Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                    startActivityForResult(intent, REQUEST_RESULT );
                }
            });

            //ショップ一覧を取得する
            try {
                doLoadShop();
            } catch (NCMBException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, REQUEST_RESULT );
        }
    }

    public static void updateInstallation(final NCMBInstallation installation) {

        //installationクラスを検索するクエリの作成
        NCMBQuery<NCMBInstallation> query = NCMBInstallation.getQuery();

        //同じRegistration IDをdeviceTokenフィールドに持つ端末情報を検索する
        query.whereEqualTo("deviceToken", installation.getDeviceToken());

        //データストアの検索を実行
        query.findInBackground(new FindCallback<NCMBInstallation>() {
            @Override
            public void done(List<NCMBInstallation> results, NCMBException e) {

                //検索された端末情報のobjectIdを設定
                installation.setObjectId(results.get(0).getObjectId());

                //端末情報を更新する
                installation.saveInBackground();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            // Start the Info activity
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            startActivityForResult(intent, REQUEST_RESULT );
            return true;
        } else if (id == R.id.action_logout) {
            doLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //**************** 【mBaaS/User④: 会員ログアウト】***************
    public void doLogout() {
        NCMBUser.logoutInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {
                    //ログインに失敗した場合の処理
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("ログアウト失敗しました。発生したエラーはこちら：" + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    //グローバル変数を初期化する
                    common.init();
                    //ログイン画面遷移する
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_RESULT );
                }
            }
        });
    }


    public void doLoadShop() throws NCMBException {
        //**************** 【mBaaS/Shop①: 「Shop」クラスのデータを取得】***************
        // 「Shop」クラスのクエリを作成
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("Shop");
        //データストアからデータを検索
        List<NCMBObject> results = query.find();
        //グローバル変数を更新する
        common.shops = results;
        ListView lv = (ListView) findViewById(R.id.lstShop);
        lv.setAdapter(new ShopListAdapter(this, results));
    }


    @Override
    public void onResume() {
        super.onResume();

        //**************** 【mBaaS：プッシュ通知⑥】リッチプッシュ通知を表示させる処理 ***************
        //リッチプッシュ通知の表示
        NCMBPush.richPushHandler(this, getIntent());

        //リッチプッシュを再表示させたくない場合はintentからURLを削除します
        getIntent().removeExtra("com.nifty.RichUrl");
    }


}
