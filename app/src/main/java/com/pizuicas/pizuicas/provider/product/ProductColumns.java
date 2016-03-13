package com.pizuicas.pizuicas.provider.product;

import android.net.Uri;
import android.provider.BaseColumns;

import com.pizuicas.pizuicas.provider.DBProvider;

/**
 * Columns for the {@code product} table.
 */
public class ProductColumns implements BaseColumns {
    public static final String TABLE_NAME = "product";
    public static final Uri CONTENT_URI = Uri.parse(DBProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String JSONOBJECT = "jsonObject";

    public static final String SHOPIFYID = "shopifyId";

    public static final String IMAGE = "image";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            JSONOBJECT,
            SHOPIFYID,
            IMAGE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(JSONOBJECT) || c.contains("." + JSONOBJECT)) return true;
            if (c.equals(SHOPIFYID) || c.contains("." + SHOPIFYID)) return true;
            if (c.equals(IMAGE) || c.contains("." + IMAGE)) return true;
        }
        return false;
    }

}
