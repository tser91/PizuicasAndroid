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

    public ProductSelection title(String... value) {
        addEquals(ProductColumns.TITLE, value);
        return this;
    }

    public ProductSelection titleNot(String... value) {
        addNotEquals(ProductColumns.TITLE, value);
        return this;
    }

    public ProductSelection titleLike(String... value) {
        addLike(ProductColumns.TITLE, value);
        return this;
    }

    public ProductSelection titleContains(String... value) {
        addContains(ProductColumns.TITLE, value);
        return this;
    }

    public ProductSelection titleStartsWith(String... value) {
        addStartsWith(ProductColumns.TITLE, value);
        return this;
    }

    public ProductSelection titleEndsWith(String... value) {
        addEndsWith(ProductColumns.TITLE, value);
        return this;
    }

    public ProductSelection orderByTitle(boolean desc) {
        orderBy(ProductColumns.TITLE, desc);
        return this;
    }

    public ProductSelection orderByTitle() {
        orderBy(ProductColumns.TITLE, false);
        return this;
    }

    public ProductSelection description(String... value) {
        addEquals(ProductColumns.DESCRIPTION, value);
        return this;
    }

    public ProductSelection descriptionNot(String... value) {
        addNotEquals(ProductColumns.DESCRIPTION, value);
        return this;
    }

    public ProductSelection descriptionLike(String... value) {
        addLike(ProductColumns.DESCRIPTION, value);
        return this;
    }

    public ProductSelection descriptionContains(String... value) {
        addContains(ProductColumns.DESCRIPTION, value);
        return this;
    }

    public ProductSelection descriptionStartsWith(String... value) {
        addStartsWith(ProductColumns.DESCRIPTION, value);
        return this;
    }

    public ProductSelection descriptionEndsWith(String... value) {
        addEndsWith(ProductColumns.DESCRIPTION, value);
        return this;
    }

    public ProductSelection orderByDescription(boolean desc) {
        orderBy(ProductColumns.DESCRIPTION, desc);
        return this;
    }

    public ProductSelection orderByDescription() {
        orderBy(ProductColumns.DESCRIPTION, false);
        return this;
    }

    public ProductSelection price(Double... value) {
        addEquals(ProductColumns.PRICE, value);
        return this;
    }

    public ProductSelection priceNot(Double... value) {
        addNotEquals(ProductColumns.PRICE, value);
        return this;
    }

    public ProductSelection priceGt(double value) {
        addGreaterThan(ProductColumns.PRICE, value);
        return this;
    }

    public ProductSelection priceGtEq(double value) {
        addGreaterThanOrEquals(ProductColumns.PRICE, value);
        return this;
    }

    public ProductSelection priceLt(double value) {
        addLessThan(ProductColumns.PRICE, value);
        return this;
    }

    public ProductSelection priceLtEq(double value) {
        addLessThanOrEquals(ProductColumns.PRICE, value);
        return this;
    }

    public ProductSelection orderByPrice(boolean desc) {
        orderBy(ProductColumns.PRICE, desc);
        return this;
    }

    public ProductSelection orderByPrice() {
        orderBy(ProductColumns.PRICE, false);
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
