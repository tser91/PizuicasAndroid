package com.pizuicas.pizuicas.provider.product;

import android.support.annotation.Nullable;

import com.pizuicas.pizuicas.provider.base.BaseModel;

/**
 * Data model for the {@code product} table.
 */
public interface ProductModel extends BaseModel {

    /**
     * Get the {@code jsonobject} value.
     * Can be {@code null}.
     */
    @Nullable
    String getJsonobject();

    /**
     * Get the {@code shopifyid} value.
     * Can be {@code null}.
     */
    @Nullable
    String getShopifyid();

    /**
     * Get the {@code image} value.
     * Can be {@code null}.
     */
    @Nullable
    byte[] getImage();
}
