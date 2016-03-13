package com.pizuicas.pizuicas.provider.product;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code product} table.
 */
public class ProductContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return ProductColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable ProductSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable ProductSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public ProductContentValues putJsonobject(@Nullable String value) {
        mContentValues.put(ProductColumns.JSONOBJECT, value);
        return this;
    }

    public ProductContentValues putJsonobjectNull() {
        mContentValues.putNull(ProductColumns.JSONOBJECT);
        return this;
    }

    public ProductContentValues putShopifyid(@Nullable String value) {
        mContentValues.put(ProductColumns.SHOPIFYID, value);
        return this;
    }

    public ProductContentValues putShopifyidNull() {
        mContentValues.putNull(ProductColumns.SHOPIFYID);
        return this;
    }

    public ProductContentValues putImage(@Nullable byte[] value) {
        mContentValues.put(ProductColumns.IMAGE, value);
        return this;
    }

    public ProductContentValues putImageNull() {
        mContentValues.putNull(ProductColumns.IMAGE);
        return this;
    }
}
