package com.pizuicas.pizuicas.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.pizuicas.pizuicas.BuildConfig;
import com.pizuicas.pizuicas.provider.address.AddressColumns;
import com.pizuicas.pizuicas.provider.client.ClientColumns;
import com.pizuicas.pizuicas.provider.product.ProductColumns;

public class DBSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_FILE_NAME = "pizuicas.db";
    // @formatter:off
    public static final String SQL_CREATE_TABLE_ADDRESS = "CREATE TABLE IF NOT EXISTS "
            + AddressColumns.TABLE_NAME + " ( "
            + AddressColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AddressColumns.ADDRESS_1 + " TEXT, "
            + AddressColumns.ADDRESS_2 + " TEXT, "
            + AddressColumns.CITY + " TEXT, "
            + AddressColumns.PROVINCE + " TEXT, "
            + AddressColumns.ZIP_CODE + " INTEGER, "
            + AddressColumns.COUNTRY_CODE + " TEXT, "
            + AddressColumns.CLIENT_ID + " INTEGER NOT NULL "
            + ", CONSTRAINT fk_client_id FOREIGN KEY (" + AddressColumns.CLIENT_ID + ") REFERENCES client (_id) ON DELETE CASCADE"
            + " );";
    public static final String SQL_CREATE_TABLE_CLIENT = "CREATE TABLE IF NOT EXISTS "
            + ClientColumns.TABLE_NAME + " ( "
            + ClientColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ClientColumns.FIRST_NAME + " TEXT, "
            + ClientColumns.LAST_NAME + " TEXT, "
            + ClientColumns.EMAIL + " TEXT "
            + " );";
    public static final String SQL_CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS "
            + ProductColumns.TABLE_NAME + " ( "
            + ProductColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ProductColumns.JSONOBJECT + " TEXT, "
            + ProductColumns.SHOPIFYID + " TEXT, "
            + ProductColumns.IMAGE + " BLOB "
            + " );";
    private static final String TAG = DBSQLiteOpenHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 4;
    private static DBSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final DBSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:on

    private DBSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new DBSQLiteOpenHelperCallbacks();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private DBSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new DBSQLiteOpenHelperCallbacks();
    }

    public static DBSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static DBSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }

    /*
     * Pre Honeycomb.
     */
    private static DBSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new DBSQLiteOpenHelper(context);
    }

    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static DBSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new DBSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_ADDRESS);
        db.execSQL(SQL_CREATE_TABLE_CLIENT);
        db.execSQL(SQL_CREATE_TABLE_PRODUCT);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
        onCreate(db);
    }
}
