package com.pizuicas.pizuicas.provider.address;

import android.net.Uri;
import android.provider.BaseColumns;

import com.pizuicas.pizuicas.provider.DBProvider;
import com.pizuicas.pizuicas.provider.client.ClientColumns;

/**
 * Columns for the {@code address} table.
 */
public class AddressColumns implements BaseColumns {
    public static final String TABLE_NAME = "address";
    public static final Uri CONTENT_URI = Uri.parse(DBProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String ADDRESS_1 = "address_1";

    public static final String ADDRESS_2 = "address_2";

    public static final String CITY = "city";

    public static final String PROVINCE = "province";

    public static final String ZIP_CODE = "zip_code";

    public static final String COUNTRY_CODE = "country_code";

    public static final String CLIENT_ID = "client_id";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            ADDRESS_1,
            ADDRESS_2,
            CITY,
            PROVINCE,
            ZIP_CODE,
            COUNTRY_CODE,
            CLIENT_ID
    };
    // @formatter:on
    public static final String PREFIX_CLIENT = TABLE_NAME + "__" + ClientColumns.TABLE_NAME;

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(ADDRESS_1) || c.contains("." + ADDRESS_1)) return true;
            if (c.equals(ADDRESS_2) || c.contains("." + ADDRESS_2)) return true;
            if (c.equals(CITY) || c.contains("." + CITY)) return true;
            if (c.equals(PROVINCE) || c.contains("." + PROVINCE)) return true;
            if (c.equals(ZIP_CODE) || c.contains("." + ZIP_CODE)) return true;
            if (c.equals(COUNTRY_CODE) || c.contains("." + COUNTRY_CODE)) return true;
            if (c.equals(CLIENT_ID) || c.contains("." + CLIENT_ID)) return true;
        }
        return false;
    }
}
