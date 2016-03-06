package com.pizuicas.pizuicas.provider.address;

import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.BaseModel;

/**
 * Data model for the {@code address} table.
 */
public interface AddressModel extends BaseModel {

    /**
     * Get the {@code address_1} value.
     * Can be {@code null}.
     */
    @Nullable
    String getAddress1();

    /**
     * Get the {@code address_2} value.
     * Can be {@code null}.
     */
    @Nullable
    String getAddress2();

    /**
     * Get the {@code city} value.
     * Can be {@code null}.
     */
    @Nullable
    String getCity();

    /**
     * Get the {@code province} value.
     * Can be {@code null}.
     */
    @Nullable
    String getProvince();

    /**
     * Get the {@code zip_code} value.
     * Can be {@code null}.
     */
    @Nullable
    Integer getZipCode();

    /**
     * Get the {@code country_code} value.
     * Can be {@code null}.
     */
    @Nullable
    String getCountryCode();

    /**
     * Get the {@code client_id} value.
     */
    long getClientId();
}
