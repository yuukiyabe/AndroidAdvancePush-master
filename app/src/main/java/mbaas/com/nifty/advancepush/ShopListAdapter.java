package mbaas.com.nifty.advancepush;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.core.FetchFileCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBFile;
import com.nifty.cloud.mb.core.NCMBObject;


public class ShopListAdapter extends BaseAdapter{


    private static final String TAG = "ShopListAdapter";
    private static final int REQUEST_RESULT = 0;
    private Common common;

    Context context;
    List<NCMBObject> shops;

    private static LayoutInflater inflater=null;
    public ShopListAdapter(MainActivity mainActivity, List<NCMBObject> tmpShops) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        shops = tmpShops;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return shops.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        NCMBObject tmpObj = shops.get(position);
        holder.tv.setText(tmpObj.getString("name"));
        String filename = tmpObj.getString("icon_image");

        //**************** 【mBaaS/File①: ショップ画像を取得】***************
        NCMBFile file = new NCMBFile(filename);
        file.fetchInBackground(new FetchFileCallback() {
            @Override
            public void done(byte[] data, NCMBException e) {
                if (e != null) {
                    // 取得失敗時の処理
                    Log.d(TAG, e.getMessage());
                } else {
                    // 取得成功時の処理
                    Bitmap bmp = null;
                    if (data != null) {
                        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    }
                    holder.img.setImageBitmap(bmp);
                }
            }
        });

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NCMBObject tmpObj = shops.get(position);
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+tmpObj.getString("name"), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ShopActivity.class);
                intent.putExtra("objectId", tmpObj.getObjectId());
                intent.putExtra("name", tmpObj.getString("name"));
                intent.putExtra("shop_image", tmpObj.getString("shop_image"));
                ((Activity)context).startActivityForResult(intent, REQUEST_RESULT);
            }
        });
        return rowView;
    }

}