package mbaas.com.nifty.advancepush;

import android.app.Application;

import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBUser;

import java.util.List;

/**
 * グローバル変数を扱うクラス.
 */
public class Common extends Application {

    // ログイン中ユーザー情報
    NCMBUser currentUser;
    // サーバーからローディングしたショップの情報
    List<NCMBObject> shops;

    /**
     * 変数を初期化する
     */
    public void init() {
        currentUser = new NCMBUser();
        shops = null;
    }
}
