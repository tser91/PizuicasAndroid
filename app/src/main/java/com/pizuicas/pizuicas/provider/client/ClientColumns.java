package com.pizuicas.pizuicas.provider.client;

import android.net.Uri;
import android.provider.BaseColumns;

import com.pizuicas.pizuicas.provider.DBProvider;

/**
 * Columns for the {@code client} table.
 */
public class ClientColumns implements BaseColumns {
    public static final String TABLE_NAME = "client";
    public static final Uri CONTENT_URI = Uri.parse(DBProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String FIRST_NAME = "first_name";

    public static final String LAST_NAME = "last_name";

    public static final String EMAIL = "email";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            FIRST_NAME,
            LAST_NAME,
            EMAIL
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(FIRST_NAME) || c.contains("." + FIRST_NAME)) return true;
            if (c.equals(LAST_NAME) || c.contains("." + LAST_NAME)) return true;
            if (c.equals(EMAIL) || c.contains("." + EMAIL)) return true;
        }
        return false;
    }

}
