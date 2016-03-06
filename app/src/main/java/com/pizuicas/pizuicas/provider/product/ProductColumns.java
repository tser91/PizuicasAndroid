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

    public static final String TITLE = "title";

    public static final String DESCRIPTION = "description";

    public static final String PRICE = "price";

    public static final String IMAGE = "image";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            TITLE,
            DESCRIPTION,
            PRICE,
            IMAGE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(DESCRIPTION) || c.contains("." + DESCRIPTION)) return true;
            if (c.equals(PRICE) || c.contains("." + PRICE)) return true;
            if (c.equals(IMAGE) || c.contains("." + IMAGE)) return true;
        }
        return false;
    }

}
