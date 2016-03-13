package com.pizuicas.pizuicas.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pizuicas.pizuicas.BuildConfig;
import com.pizuicas.pizuicas.provider.address.AddressColumns;
import com.pizuicas.pizuicas.provider.client.ClientColumns;
import com.pizuicas.pizuicas.provider.product.ProductColumns;

/**
 * Implement your custom database creation or upgrade code here.
 *
 * This file will not be overwritten if you re-run the content provider generator.
 */
public class DBSQLiteOpenHelperCallbacks {
    private static final String TAG = DBSQLiteOpenHelperCallbacks.class.getSimpleName();

    public void onOpen(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onOpen");
        // Insert your db open code here.
    }

    public void onPreCreate(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onPreCreate");
        // Insert your db creation code here. This is called before your tables are created.
    }

    public void onPostCreate(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onPostCreate");
        // Insert your db creation code here. This is called after your tables are created.
    }

    public void onUpgrade(final Context context, final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + AddressColumns.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ClientColumns.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ProductColumns.TABLE_NAME);
        }
    }
}
