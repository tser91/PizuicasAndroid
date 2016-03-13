package com.pizuicas.pizuicas.provider.product;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code product} table.
 */
public class ProductCursor extends AbstractCursor implements ProductModel {
    public ProductCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(ProductColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code jsonobject} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getJsonobject() {
        String res = getStringOrNull(ProductColumns.JSONOBJECT);
        return res;
    }

    /**
     * Get the {@code shopifyid} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getShopifyid() {
        String res = getStringOrNull(ProductColumns.SHOPIFYID);
        return res;
    }

    /**
     * Get the {@code image} value.
     * Can be {@code null}.
     */
    @Nullable
    public byte[] getImage() {
        byte[] res = getBlobOrNull(ProductColumns.IMAGE);
        return res;
    }
}
