package mbaas.com.nifty.advancepush;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.nifty.cloud.mb.core.NCMBObject;

import java.util.List;

public class ShopListFavoriteAdapter extends BaseAdapter {

    private Common common;
    private Context context = null;
    List<NCMBObject> shops;

    private int resource = 0;
    private int listPosition;
    private static final String TAG = "ShopListFavoriteAdapter";
    public Activity activity;

    public ShopListFavoriteAdapter(Context context,List<NCMBObject> tmpShops ,int resource, Activity act){
        this.activity = act;
        this.context = context;
        this.shops = tmpShops;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public Object getItem(int i) {
        return shops.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // グローバル変数を扱うクラスを取得する
        common = (Common) activity.getApplication();
        listPosition = i;
        final NCMBObject tmpShop = shops.get(i);
        final List<String> favList = common.currentUser.getList("favorite");

        final Activity activity =  (Activity)context;
        RelativeLayout v = (RelativeLayout)activity.getLayoutInflater().inflate(resource,null);
        ((TextView)v.findViewById(R.id.textView)).setText(tmpShop.getString("name"));

        final Switch aSwitch = (Switch)v.findViewById(R.id.favSwitch);
        if (favList.contains(tmpShop.getObjectId())) {
            aSwitch.setChecked(true);
        } else  {
            aSwitch.setChecked(false);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<String> curFavList = common.currentUser.getList("favorite");

                if(isChecked){
                    if (!curFavList.contains(tmpShop.getObjectId())) {
                        curFavList.add(tmpShop.getObjectId());
                    }
                    common.currentUser.put("favorite", curFavList);
                    Log.d(TAG, "true");
                } else {
                    if (curFavList.contains(tmpShop.getObjectId())) {
                        curFavList.remove(tmpShop.getObjectId());
                    }
                    common.currentUser.put("favorite", curFavList);
                    Log.d(TAG," false");
                }
            }
        });

        return v;
    }
}