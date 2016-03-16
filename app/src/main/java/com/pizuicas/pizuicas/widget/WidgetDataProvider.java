package com.pizuicas.pizuicas.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.pizuicas.pizuicas.R;
import com.pizuicas.pizuicas.provider.product.ProductColumns;
import com.pizuicas.pizuicas.provider.product.ProductCursor;
import com.shopify.buy.model.Product;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private final String TAG = WidgetDataProvider.class.getName();
    Context mContext = null;
    private Cursor cursor = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if (cursor == null){
            return 0;
        }
        return cursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }
        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_item_content);

        ProductCursor productCursor = new ProductCursor(cursor);
        Product product = Product.fromJson(productCursor.getJsonobject());

        Log.d(TAG, "getViewAt: " + position + " " + product.getTitle());

        views.setTextViewText(R.id.widget_item_title, product.getTitle());
        views.setContentDescription(R.id.widget_item_title, product.getTitle());

        views.setTextViewText(R.id.widget_item_price, product.getVariants().get(0).getPrice());
        views.setContentDescription(R.id.widget_item_price, product.getVariants().get(0).getPrice());

        String itemDescription;
        itemDescription = product.getBodyHtml();
        itemDescription = itemDescription.replaceAll("<br>", "\n");

        views.setTextViewText(R.id.widget_item_description, itemDescription);
        views.setContentDescription(R.id.widget_item_description, itemDescription);


        return views;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged: ");
        if (cursor != null) {
            cursor.close();
        }
        // Reset the identity of the incoming IPC on the current thread.
        long token = Binder.clearCallingIdentity();

        String[] projection = {ProductColumns.JSONOBJECT};

        cursor = mContext.getContentResolver().query(ProductColumns.CONTENT_URI, projection,
                null, null, null);


        // Restore the identity of the incoming IPC on the current thread back to a
        // previously identity that was returned by clearCallingIdentity function
        Binder.restoreCallingIdentity(token);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

}