package com.pizuicas.pizuicas.provider.product;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pizuicas.pizuicas.provider.base.AbstractSelection;

/**
 * Selection for the {@code product} table.
 */
public class ProductSelection extends AbstractSelection<ProductSelection> {
    @Override
    protected Uri baseUri() {
        return ProductColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code ProductCursor} object, which is positioned before the first entry, or null.
     */
    public ProductCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new ProductCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public ProductCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code ProductCursor} object, which is positioned before the first entry, or null.
     */
    public ProductCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new ProductCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public ProductCursor query(Context context) {
        return query(context, null);
    }


    public ProductSelection id(long... value) {
        addEquals("product." + ProductColumns._ID, toObjectArray(value));
        return this;
    }

    public ProductSelection idNot(long... value) {
        addNotEquals("product." + ProductColumns._ID, toObjectArray(value));
        return this;
    }

    public ProductSelection orderById(boolean desc) {
        orderBy("product." + ProductColumns._ID, desc);
        return this;
    }

    public ProductSelection orderById() {
        return orderById(false);
    }

    public ProductSelection jsonobject(String... value) {
        addEquals(ProductColumns.JSONOBJECT, value);
        return this;
    }

    public ProductSelection jsonobjectNot(String... value) {
        addNotEquals(ProductColumns.JSONOBJECT, value);
        return this;
    }

    public ProductSelection jsonobjectLike(String... value) {
        addLike(ProductColumns.JSONOBJECT, value);
        return this;
    }

    public ProductSelection jsonobjectContains(String... value) {
        addContains(ProductColumns.JSONOBJECT, value);
        return this;
    }

    public ProductSelection jsonobjectStartsWith(String... value) {
        addStartsWith(ProductColumns.JSONOBJECT, value);
        return this;
    }

    public ProductSelection jsonobjectEndsWith(String... value) {
        addEndsWith(ProductColumns.JSONOBJECT, value);
        return this;
    }

    public ProductSelection orderByJsonobject(boolean desc) {
        orderBy(ProductColumns.JSONOBJECT, desc);
        return this;
    }

    public ProductSelection orderByJsonobject() {
        orderBy(ProductColumns.JSONOBJECT, false);
        return this;
    }

    public ProductSelection shopifyid(String... value) {
        addEquals(ProductColumns.SHOPIFYID, value);
        return this;
    }

    public ProductSelection shopifyidNot(String... value) {
        addNotEquals(ProductColumns.SHOPIFYID, value);
        return this;
    }

    public ProductSelection shopifyidLike(String... value) {
        addLike(ProductColumns.SHOPIFYID, value);
        return this;
    }

    public ProductSelection shopifyidContains(String... value) {
        addContains(ProductColumns.SHOPIFYID, value);
        return this;
    }

    public ProductSelection shopifyidStartsWith(String... value) {
        addStartsWith(ProductColumns.SHOPIFYID, value);
        return this;
    }

    public ProductSelection shopifyidEndsWith(String... value) {
        addEndsWith(ProductColumns.SHOPIFYID, value);
        return this;
    }

    public ProductSelection orderByShopifyid(boolean desc) {
        orderBy(ProductColumns.SHOPIFYID, desc);
        return this;
    }

    public ProductSelection orderByShopifyid() {
        orderBy(ProductColumns.SHOPIFYID, false);
        return this;
    }

    public ProductSelection image(byte[]... value) {
        addEquals(ProductColumns.IMAGE, value);
        return this;
    }

    public ProductSelection imageNot(byte[]... value) {
        addNotEquals(ProductColumns.IMAGE, value);
        return this;
    }


    public ProductSelection orderByImage(boolean desc) {
        orderBy(ProductColumns.IMAGE, desc);
        return this;
    }

    public ProductSelection orderByImage() {
        orderBy(ProductColumns.IMAGE, false);
        return this;
    }
}
