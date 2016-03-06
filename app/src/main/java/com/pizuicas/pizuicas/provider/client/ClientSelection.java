package com.pizuicas.pizuicas.provider.client;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pizuicas.pizuicas.provider.base.AbstractSelection;

/**
 * Selection for the {@code client} table.
 */
public class ClientSelection extends AbstractSelection<ClientSelection> {
    @Override
    protected Uri baseUri() {
        return ClientColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code ClientCursor} object, which is positioned before the first entry, or null.
     */
    public ClientCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new ClientCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public ClientCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code ClientCursor} object, which is positioned before the first entry, or null.
     */
    public ClientCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new ClientCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public ClientCursor query(Context context) {
        return query(context, null);
    }


    public ClientSelection id(long... value) {
        addEquals("client." + ClientColumns._ID, toObjectArray(value));
        return this;
    }

    public ClientSelection idNot(long... value) {
        addNotEquals("client." + ClientColumns._ID, toObjectArray(value));
        return this;
    }

    public ClientSelection orderById(boolean desc) {
        orderBy("client." + ClientColumns._ID, desc);
        return this;
    }

    public ClientSelection orderById() {
        return orderById(false);
    }

    public ClientSelection firstName(String... value) {
        addEquals(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public ClientSelection firstNameNot(String... value) {
        addNotEquals(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public ClientSelection firstNameLike(String... value) {
        addLike(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public ClientSelection firstNameContains(String... value) {
        addContains(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public ClientSelection firstNameStartsWith(String... value) {
        addStartsWith(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public ClientSelection firstNameEndsWith(String... value) {
        addEndsWith(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public ClientSelection orderByFirstName(boolean desc) {
        orderBy(ClientColumns.FIRST_NAME, desc);
        return this;
    }

    public ClientSelection orderByFirstName() {
        orderBy(ClientColumns.FIRST_NAME, false);
        return this;
    }

    public ClientSelection lastName(String... value) {
        addEquals(ClientColumns.LAST_NAME, value);
        return this;
    }

    public ClientSelection lastNameNot(String... value) {
        addNotEquals(ClientColumns.LAST_NAME, value);
        return this;
    }

    public ClientSelection lastNameLike(String... value) {
        addLike(ClientColumns.LAST_NAME, value);
        return this;
    }

    public ClientSelection lastNameContains(String... value) {
        addContains(ClientColumns.LAST_NAME, value);
        return this;
    }

    public ClientSelection lastNameStartsWith(String... value) {
        addStartsWith(ClientColumns.LAST_NAME, value);
        return this;
    }

    public ClientSelection lastNameEndsWith(String... value) {
        addEndsWith(ClientColumns.LAST_NAME, value);
        return this;
    }

    public ClientSelection orderByLastName(boolean desc) {
        orderBy(ClientColumns.LAST_NAME, desc);
        return this;
    }

    public ClientSelection orderByLastName() {
        orderBy(ClientColumns.LAST_NAME, false);
        return this;
    }

    public ClientSelection email(String... value) {
        addEquals(ClientColumns.EMAIL, value);
        return this;
    }

    public ClientSelection emailNot(String... value) {
        addNotEquals(ClientColumns.EMAIL, value);
        return this;
    }

    public ClientSelection emailLike(String... value) {
        addLike(ClientColumns.EMAIL, value);
        return this;
    }

    public ClientSelection emailContains(String... value) {
        addContains(ClientColumns.EMAIL, value);
        return this;
    }

    public ClientSelection emailStartsWith(String... value) {
        addStartsWith(ClientColumns.EMAIL, value);
        return this;
    }

    public ClientSelection emailEndsWith(String... value) {
        addEndsWith(ClientColumns.EMAIL, value);
        return this;
    }

    public ClientSelection orderByEmail(boolean desc) {
        orderBy(ClientColumns.EMAIL, desc);
        return this;
    }

    public ClientSelection orderByEmail() {
        orderBy(ClientColumns.EMAIL, false);
        return this;
    }
}
