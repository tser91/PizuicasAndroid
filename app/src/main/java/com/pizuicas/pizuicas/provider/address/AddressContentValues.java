package com.pizuicas.pizuicas.provider.address;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code address} table.
 */
public class AddressContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return AddressColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable AddressSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable AddressSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public AddressContentValues putAddress1(@Nullable String value) {
        mContentValues.put(AddressColumns.ADDRESS_1, value);
        return this;
    }

    public AddressContentValues putAddress1Null() {
        mContentValues.putNull(AddressColumns.ADDRESS_1);
        return this;
    }

    public AddressContentValues putAddress2(@Nullable String value) {
        mContentValues.put(AddressColumns.ADDRESS_2, value);
        return this;
    }

    public AddressContentValues putAddress2Null() {
        mContentValues.putNull(AddressColumns.ADDRESS_2);
        return this;
    }

    public AddressContentValues putCity(@Nullable String value) {
        mContentValues.put(AddressColumns.CITY, value);
        return this;
    }

    public AddressContentValues putCityNull() {
        mContentValues.putNull(AddressColumns.CITY);
        return this;
    }

    public AddressContentValues putProvince(@Nullable String value) {
        mContentValues.put(AddressColumns.PROVINCE, value);
        return this;
    }

    public AddressContentValues putProvinceNull() {
        mContentValues.putNull(AddressColumns.PROVINCE);
        return this;
    }

    public AddressContentValues putZipCode(@Nullable Integer value) {
        mContentValues.put(AddressColumns.ZIP_CODE, value);
        return this;
    }

    public AddressContentValues putZipCodeNull() {
        mContentValues.putNull(AddressColumns.ZIP_CODE);
        return this;
    }

    public AddressContentValues putCountryCode(@Nullable String value) {
        mContentValues.put(AddressColumns.COUNTRY_CODE, value);
        return this;
    }

    public AddressContentValues putCountryCodeNull() {
        mContentValues.putNull(AddressColumns.COUNTRY_CODE);
        return this;
    }

    public AddressContentValues putClientId(long value) {
        mContentValues.put(AddressColumns.CLIENT_ID, value);
        return this;
    }

}
