package com.pizuicas.pizuicas.provider.address;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.AbstractCursor;
import com.pizuicas.pizuicas.provider.client.ClientColumns;

/**
 * Cursor wrapper for the {@code address} table.
 */
public class AddressCursor extends AbstractCursor implements AddressModel {
    public AddressCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(AddressColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code address_1} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getAddress1() {
        String res = getStringOrNull(AddressColumns.ADDRESS_1);
        return res;
    }

    /**
     * Get the {@code address_2} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getAddress2() {
        String res = getStringOrNull(AddressColumns.ADDRESS_2);
        return res;
    }

    /**
     * Get the {@code city} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getCity() {
        String res = getStringOrNull(AddressColumns.CITY);
        return res;
    }

    /**
     * Get the {@code province} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getProvince() {
        String res = getStringOrNull(AddressColumns.PROVINCE);
        return res;
    }

    /**
     * Get the {@code zip_code} value.
     * Can be {@code null}.
     */
    @Nullable
    public Integer getZipCode() {
        Integer res = getIntegerOrNull(AddressColumns.ZIP_CODE);
        return res;
    }

    /**
     * Get the {@code country_code} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getCountryCode() {
        String res = getStringOrNull(AddressColumns.COUNTRY_CODE);
        return res;
    }

    /**
     * Get the {@code client_id} value.
     */
    public long getClientId() {
        Long res = getLongOrNull(AddressColumns.CLIENT_ID);
        if (res == null)
            throw new NullPointerException("The value of 'client_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code first_name} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getClientFirstName() {
        String res = getStringOrNull(ClientColumns.FIRST_NAME);
        return res;
    }

    /**
     * Get the {@code last_name} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getClientLastName() {
        String res = getStringOrNull(ClientColumns.LAST_NAME);
        return res;
    }

    /**
     * Get the {@code email} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getClientEmail() {
        String res = getStringOrNull(ClientColumns.EMAIL);
        return res;
    }
}
