package com.pizuicas.pizuicas;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import com.shopify.buy.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public abstract class ProductImageRetriever extends AsyncTask {

    private final String TAG = ProductImageRetriever.class.getName();

    private Product mItem;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private byte[] getBytesFromInputStream(InputStream is) throws IOException
    {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();)
        {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        mItem = (Product) params[0];
        try {

            URL url= new URL(mItem.getImages().get(0).getSrc());
            Log.d(TAG, "doInBackground: url: "+ url);

            byte[] imageBytes = getBytesFromInputStream(
                    (InputStream) new URL(url.toString()).getContent());
            Log.d(TAG, "doInBackground: productId: "+mItem.getProductId()+ ", prod title: "+ mItem.getTitle());
            Log.d(TAG, "doInBackground: imagebytes: "+ imageBytes.toString());

            return Pair.create(mItem.getProductId(), imageBytes);
        } catch (Exception e) {
            return null;
        }
    }

    abstract void onPostExecute(Pair result);
}