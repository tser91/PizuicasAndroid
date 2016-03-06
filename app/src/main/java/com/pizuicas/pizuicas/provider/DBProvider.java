package com.pizuicas.pizuicas.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.pizuicas.pizuicas.BuildConfig;
import com.pizuicas.pizuicas.provider.address.AddressColumns;
import com.pizuicas.pizuicas.provider.base.BaseContentProvider;
import com.pizuicas.pizuicas.provider.client.ClientColumns;
import com.pizuicas.pizuicas.provider.product.ProductColumns;

import java.util.Arrays;

public class DBProvider extends BaseContentProvider {
    public static final String AUTHORITY = "com.pizuicas.pizuicas.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;
    private static final String TAG = DBProvider.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";
    private static final int URI_TYPE_ADDRESS = 0;
    private static final int URI_TYPE_ADDRESS_ID = 1;

    private static final int URI_TYPE_CLIENT = 2;
    private static final int URI_TYPE_CLIENT_ID = 3;

    private static final int URI_TYPE_PRODUCT = 4;
    private static final int URI_TYPE_PRODUCT_ID = 5;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, AddressColumns.TABLE_NAME, URI_TYPE_ADDRESS);
        URI_MATCHER.addURI(AUTHORITY, AddressColumns.TABLE_NAME + "/#", URI_TYPE_ADDRESS_ID);
        URI_MATCHER.addURI(AUTHORITY, ClientColumns.TABLE_NAME, URI_TYPE_CLIENT);
        URI_MATCHER.addURI(AUTHORITY, ClientColumns.TABLE_NAME + "/#", URI_TYPE_CLIENT_ID);
        URI_MATCHER.addURI(AUTHORITY, ProductColumns.TABLE_NAME, URI_TYPE_PRODUCT);
        URI_MATCHER.addURI(AUTHORITY, ProductColumns.TABLE_NAME + "/#", URI_TYPE_PRODUCT_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return DBSQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_ADDRESS:
                return TYPE_CURSOR_DIR + AddressColumns.TABLE_NAME;
            case URI_TYPE_ADDRESS_ID:
                return TYPE_CURSOR_ITEM + AddressColumns.TABLE_NAME;

            case URI_TYPE_CLIENT:
                return TYPE_CURSOR_DIR + ClientColumns.TABLE_NAME;
            case URI_TYPE_CLIENT_ID:
                return TYPE_CURSOR_ITEM + ClientColumns.TABLE_NAME;

            case URI_TYPE_PRODUCT:
                return TYPE_CURSOR_DIR + ProductColumns.TABLE_NAME;
            case URI_TYPE_PRODUCT_ID:
                return TYPE_CURSOR_ITEM + ProductColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_ADDRESS:
            case URI_TYPE_ADDRESS_ID:
                res.table = AddressColumns.TABLE_NAME;
                res.idColumn = AddressColumns._ID;
                res.tablesWithJoins = AddressColumns.TABLE_NAME;
                if (ClientColumns.hasColumns(projection)) {
                    res.tablesWithJoins += " LEFT OUTER JOIN " + ClientColumns.TABLE_NAME + " AS " + AddressColumns.PREFIX_CLIENT + " ON " + AddressColumns.TABLE_NAME + "." + AddressColumns.CLIENT_ID + "=" + AddressColumns.PREFIX_CLIENT + "." + ClientColumns._ID;
                }
                res.orderBy = AddressColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_CLIENT:
            case URI_TYPE_CLIENT_ID:
                res.table = ClientColumns.TABLE_NAME;
                res.idColumn = ClientColumns._ID;
                res.tablesWithJoins = ClientColumns.TABLE_NAME;
                res.orderBy = ClientColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_PRODUCT:
            case URI_TYPE_PRODUCT_ID:
                res.table = ProductColumns.TABLE_NAME;
                res.idColumn = ProductColumns._ID;
                res.tablesWithJoins = ProductColumns.TABLE_NAME;
                res.orderBy = ProductColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_ADDRESS_ID:
            case URI_TYPE_CLIENT_ID:
            case URI_TYPE_PRODUCT_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
