package com.pizuicas.pizuicas.provider.client;

import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.BaseModel;

/**
 * Data model for the {@code client} table.
 */
public interface ClientModel extends BaseModel {

    /**
     * Get the {@code first_name} value.
     * Can be {@code null}.
     */
    @Nullable
    String getFirstName();

    /**
     * Get the {@code last_name} value.
     * Can be {@code null}.
     */
    @Nullable
    String getLastName();

    /**
     * Get the {@code email} value.
     * Can be {@code null}.
     */
    @Nullable
    String getEmail();
}
