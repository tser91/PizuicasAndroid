package com.pizuicas.pizuicas.provider.client;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code client} table.
 */
public class ClientCursor extends AbstractCursor implements ClientModel {
    public ClientCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(ClientColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code first_name} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getFirstName() {
        String res = getStringOrNull(ClientColumns.FIRST_NAME);
        return res;
    }

    /**
     * Get the {@code last_name} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getLastName() {
        String res = getStringOrNull(ClientColumns.LAST_NAME);
        return res;
    }

    /**
     * Get the {@code email} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getEmail() {
        String res = getStringOrNull(ClientColumns.EMAIL);
        return res;
    }
}
