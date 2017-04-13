package mbaas.com.nifty.advancepush;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nifty.cloud.mb.core.LoginCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBFacebookParameters;
import com.nifty.cloud.mb.core.NCMBUser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;

    LoginButton loginButton;
    GraphRequestAsyncTask graphRequest;
    String data;
    private GoogleApiClient client;

    private Common common;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_RESULT = 0;

    EditText _loginEmail;
    EditText _loginPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // グローバル変数を扱うクラスを取得する
        common = (Common) getApplication();

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);

        //TextViewをクリッカブルにする

        // <a>タグを含めたテキストを用意します

        _loginEmail = (EditText) findViewById(R.id.txtEmail);
        _loginPassword = (EditText) findViewById(R.id.txtPassword);

        Button _loginButton = (Button) findViewById(R.id.btnLogin);
        Button _signupButton = (Button) findViewById(R.id.btnSignup);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // サインアップ画面遷移する
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ログイン画面遷移する
                doLogin();
            }
        });

        //AppEventsLogger.activateApp(this);
        loginButton =(LoginButton)findViewById(R.id.button2);
        //loginButton.setReadPermissions("user_posts");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));
        callbackManager = CallbackManager.Factory.create();


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        accessToken=AccessToken.getCurrentAccessToken();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };


        /*LoginManager.getInstance()*///loginButton.registerCallback(callbackManager,
            /*    new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        //Login to NIFTY Cloud mobile backend
                        final Profile profile=Profile.getCurrentProfile();
                        final NCMBUser user=new NCMBUser();

                        user.setUserName(profile.getName().toString());


                        NCMBFacebookParameters parameters = new NCMBFacebookParameters(
                                loginResult.getAccessToken().getUserId(),
                                loginResult.getAccessToken().getToken(),
                                loginResult.getAccessToken().getExpires()
                        );

                        user.loginInBackgroundWith(parameters, new LoginCallback() {
                            @Override
                            public void done(NCMBUser user, NCMBException e) {
                                if (e != null) {
                                    Assert.fail(e.getMessage());
                                }
                            }
                        });


                        try {
                            user.loginWith(parameters);
                            Toast.makeText(getApplicationContext(), "Login to NIFTY Cloud mbaas with Facebook account", Toast.LENGTH_LONG).show();
                        } catch (NCMBException e) {
                            e.printStackTrace();
                        }

                        GraphRequest request=GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback(){

                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        try {
                                            String email = object.getString("email");
                                            String birthday = object.getString("birthday");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                        );
                        Bundle parameter =new Bundle();
                        parameter.putString("fields","id,name,email,birthday,gender");
                        request.setParameters(parameter);
                        request.executeAsync();

                        TextView textView=(TextView) findViewById(R.id.textView3);


                        //textView.setText(profile.getName().toString());
                        //textView.setText(user.getUserName().toString());



                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("tag", "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("tag", "onError:" + exception);
                    }
                });

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email","user_birthday"));



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();*/
    }




    protected void doLogin() {
        //**************** 【mBaaS/User②】: メールアドレスとパスワードでログイン】***************
        String email = _loginEmail.getText().toString();
        String password = _loginPassword.getText().toString();

        NCMBUser.loginWithMailAddressInBackground(email, password, new LoginCallback() {
            @Override
            public void done(NCMBUser user, NCMBException e) {
                if (e != null) {
                    //ログインに失敗した場合の処理
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("Login failed! Error:" + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    //ログインに成功した場合の処理
                    common.currentUser = NCMBUser.getCurrentUser();
                    AlertDialog show = new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Notification from Nifty")
                            .setMessage("ログイン成功")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String nickname = common.currentUser.getString("nickname");
                                    if (nickname != null && !nickname.isEmpty() && !nickname.equals("null")) {
                                        //メイン画面遷移します
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivityForResult(intent, REQUEST_RESULT);
                                    } else {
                                        //初期ログイン会員登録画面遷移します
                                        Toast.makeText(LoginActivity.this, "Register user information for the first time!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                        startActivityForResult(intent, REQUEST_RESULT);
                                    }
                                }
                            })
                            .show();
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onDestroy(){
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }




    private void GraphTest(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{user-birthday}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback(){
                    public void onCompleted(GraphResponse response){
                        Log.i(TAG, response.getJSONObject().toString());
                    }
                }
        ).executeAsync();
    }

    public void  onClick()
    {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
