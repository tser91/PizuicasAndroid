package com.pizuicas.pizuicas.provider.product;

import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.BaseModel;

/**
 * Data model for the {@code product} table.
 */
public interface ProductModel extends BaseModel {

    /**
     * Get the {@code title} value.
     * Can be {@code null}.
     */
    @Nullable
    String getTitle();

    /**
     * Get the {@code description} value.
     * Can be {@code null}.
     */
    @Nullable
    String getDescription();

    /**
     * Get the {@code price} value.
     * Can be {@code null}.
     */
    @Nullable
    Double getPrice();

    /**
     * Get the {@code image} value.
     * Can be {@code null}.
     */
    @Nullable
    byte[] getImage();
}
