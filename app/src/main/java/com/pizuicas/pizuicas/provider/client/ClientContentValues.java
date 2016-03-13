package com.pizuicas.pizuicas.provider.client;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code client} table.
 */
public class ClientContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return ClientColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable ClientSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable ClientSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public ClientContentValues putFirstName(@Nullable String value) {
        mContentValues.put(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public ClientContentValues putFirstNameNull() {
        mContentValues.putNull(ClientColumns.FIRST_NAME);
        return this;
    }

    public ClientContentValues putLastName(@Nullable String value) {
        mContentValues.put(ClientColumns.LAST_NAME, value);
        return this;
    }

    public ClientContentValues putLastNameNull() {
        mContentValues.putNull(ClientColumns.LAST_NAME);
        return this;
    }

    public ClientContentValues putEmail(@Nullable String value) {
        mContentValues.put(ClientColumns.EMAIL, value);
        return this;
    }

    public ClientContentValues putEmailNull() {
        mContentValues.putNull(ClientColumns.EMAIL);
        return this;
    }
}
