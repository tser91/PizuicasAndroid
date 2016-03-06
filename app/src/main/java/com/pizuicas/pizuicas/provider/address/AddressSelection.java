package com.pizuicas.pizuicas.provider.address;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pizuicas.pizuicas.provider.base.AbstractSelection;
import com.pizuicas.pizuicas.provider.client.ClientColumns;

/**
 * Selection for the {@code address} table.
 */
public class AddressSelection extends AbstractSelection<AddressSelection> {
    @Override
    protected Uri baseUri() {
        return AddressColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code AddressCursor} object, which is positioned before the first entry, or null.
     */
    public AddressCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new AddressCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public AddressCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code AddressCursor} object, which is positioned before the first entry, or null.
     */
    public AddressCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new AddressCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public AddressCursor query(Context context) {
        return query(context, null);
    }


    public AddressSelection id(long... value) {
        addEquals("address." + AddressColumns._ID, toObjectArray(value));
        return this;
    }

    public AddressSelection idNot(long... value) {
        addNotEquals("address." + AddressColumns._ID, toObjectArray(value));
        return this;
    }

    public AddressSelection orderById(boolean desc) {
        orderBy("address." + AddressColumns._ID, desc);
        return this;
    }

    public AddressSelection orderById() {
        return orderById(false);
    }

    public AddressSelection address1(String... value) {
        addEquals(AddressColumns.ADDRESS_1, value);
        return this;
    }

    public AddressSelection address1Not(String... value) {
        addNotEquals(AddressColumns.ADDRESS_1, value);
        return this;
    }

    public AddressSelection address1Like(String... value) {
        addLike(AddressColumns.ADDRESS_1, value);
        return this;
    }

    public AddressSelection address1Contains(String... value) {
        addContains(AddressColumns.ADDRESS_1, value);
        return this;
    }

    public AddressSelection address1StartsWith(String... value) {
        addStartsWith(AddressColumns.ADDRESS_1, value);
        return this;
    }

    public AddressSelection address1EndsWith(String... value) {
        addEndsWith(AddressColumns.ADDRESS_1, value);
        return this;
    }

    public AddressSelection orderByAddress1(boolean desc) {
        orderBy(AddressColumns.ADDRESS_1, desc);
        return this;
    }

    public AddressSelection orderByAddress1() {
        orderBy(AddressColumns.ADDRESS_1, false);
        return this;
    }

    public AddressSelection address2(String... value) {
        addEquals(AddressColumns.ADDRESS_2, value);
        return this;
    }

    public AddressSelection address2Not(String... value) {
        addNotEquals(AddressColumns.ADDRESS_2, value);
        return this;
    }

    public AddressSelection address2Like(String... value) {
        addLike(AddressColumns.ADDRESS_2, value);
        return this;
    }

    public AddressSelection address2Contains(String... value) {
        addContains(AddressColumns.ADDRESS_2, value);
        return this;
    }

    public AddressSelection address2StartsWith(String... value) {
        addStartsWith(AddressColumns.ADDRESS_2, value);
        return this;
    }

    public AddressSelection address2EndsWith(String... value) {
        addEndsWith(AddressColumns.ADDRESS_2, value);
        return this;
    }

    public AddressSelection orderByAddress2(boolean desc) {
        orderBy(AddressColumns.ADDRESS_2, desc);
        return this;
    }

    public AddressSelection orderByAddress2() {
        orderBy(AddressColumns.ADDRESS_2, false);
        return this;
    }

    public AddressSelection city(String... value) {
        addEquals(AddressColumns.CITY, value);
        return this;
    }

    public AddressSelection cityNot(String... value) {
        addNotEquals(AddressColumns.CITY, value);
        return this;
    }

    public AddressSelection cityLike(String... value) {
        addLike(AddressColumns.CITY, value);
        return this;
    }

    public AddressSelection cityContains(String... value) {
        addContains(AddressColumns.CITY, value);
        return this;
    }

    public AddressSelection cityStartsWith(String... value) {
        addStartsWith(AddressColumns.CITY, value);
        return this;
    }

    public AddressSelection cityEndsWith(String... value) {
        addEndsWith(AddressColumns.CITY, value);
        return this;
    }

    public AddressSelection orderByCity(boolean desc) {
        orderBy(AddressColumns.CITY, desc);
        return this;
    }

    public AddressSelection orderByCity() {
        orderBy(AddressColumns.CITY, false);
        return this;
    }

    public AddressSelection province(String... value) {
        addEquals(AddressColumns.PROVINCE, value);
        return this;
    }

    public AddressSelection provinceNot(String... value) {
        addNotEquals(AddressColumns.PROVINCE, value);
        return this;
    }

    public AddressSelection provinceLike(String... value) {
        addLike(AddressColumns.PROVINCE, value);
        return this;
    }

    public AddressSelection provinceContains(String... value) {
        addContains(AddressColumns.PROVINCE, value);
        return this;
    }

    public AddressSelection provinceStartsWith(String... value) {
        addStartsWith(AddressColumns.PROVINCE, value);
        return this;
    }

    public AddressSelection provinceEndsWith(String... value) {
        addEndsWith(AddressColumns.PROVINCE, value);
        return this;
    }

    public AddressSelection orderByProvince(boolean desc) {
        orderBy(AddressColumns.PROVINCE, desc);
        return this;
    }

    public AddressSelection orderByProvince() {
        orderBy(AddressColumns.PROVINCE, false);
        return this;
    }

    public AddressSelection zipCode(Integer... value) {
        addEquals(AddressColumns.ZIP_CODE, value);
        return this;
    }

    public AddressSelection zipCodeNot(Integer... value) {
        addNotEquals(AddressColumns.ZIP_CODE, value);
        return this;
    }

    public AddressSelection zipCodeGt(int value) {
        addGreaterThan(AddressColumns.ZIP_CODE, value);
        return this;
    }

    public AddressSelection zipCodeGtEq(int value) {
        addGreaterThanOrEquals(AddressColumns.ZIP_CODE, value);
        return this;
    }

    public AddressSelection zipCodeLt(int value) {
        addLessThan(AddressColumns.ZIP_CODE, value);
        return this;
    }

    public AddressSelection zipCodeLtEq(int value) {
        addLessThanOrEquals(AddressColumns.ZIP_CODE, value);
        return this;
    }

    public AddressSelection orderByZipCode(boolean desc) {
        orderBy(AddressColumns.ZIP_CODE, desc);
        return this;
    }

    public AddressSelection orderByZipCode() {
        orderBy(AddressColumns.ZIP_CODE, false);
        return this;
    }

    public AddressSelection countryCode(String... value) {
        addEquals(AddressColumns.COUNTRY_CODE, value);
        return this;
    }

    public AddressSelection countryCodeNot(String... value) {
        addNotEquals(AddressColumns.COUNTRY_CODE, value);
        return this;
    }

    public AddressSelection countryCodeLike(String... value) {
        addLike(AddressColumns.COUNTRY_CODE, value);
        return this;
    }

    public AddressSelection countryCodeContains(String... value) {
        addContains(AddressColumns.COUNTRY_CODE, value);
        return this;
    }

    public AddressSelection countryCodeStartsWith(String... value) {
        addStartsWith(AddressColumns.COUNTRY_CODE, value);
        return this;
    }

    public AddressSelection countryCodeEndsWith(String... value) {
        addEndsWith(AddressColumns.COUNTRY_CODE, value);
        return this;
    }

    public AddressSelection orderByCountryCode(boolean desc) {
        orderBy(AddressColumns.COUNTRY_CODE, desc);
        return this;
    }

    public AddressSelection orderByCountryCode() {
        orderBy(AddressColumns.COUNTRY_CODE, false);
        return this;
    }

    public AddressSelection clientId(long... value) {
        addEquals(AddressColumns.CLIENT_ID, toObjectArray(value));
        return this;
    }

    public AddressSelection clientIdNot(long... value) {
        addNotEquals(AddressColumns.CLIENT_ID, toObjectArray(value));
        return this;
    }

    public AddressSelection clientIdGt(long value) {
        addGreaterThan(AddressColumns.CLIENT_ID, value);
        return this;
    }

    public AddressSelection clientIdGtEq(long value) {
        addGreaterThanOrEquals(AddressColumns.CLIENT_ID, value);
        return this;
    }

    public AddressSelection clientIdLt(long value) {
        addLessThan(AddressColumns.CLIENT_ID, value);
        return this;
    }

    public AddressSelection clientIdLtEq(long value) {
        addLessThanOrEquals(AddressColumns.CLIENT_ID, value);
        return this;
    }

    public AddressSelection orderByClientId(boolean desc) {
        orderBy(AddressColumns.CLIENT_ID, desc);
        return this;
    }

    public AddressSelection orderByClientId() {
        orderBy(AddressColumns.CLIENT_ID, false);
        return this;
    }

    public AddressSelection clientFirstName(String... value) {
        addEquals(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public AddressSelection clientFirstNameNot(String... value) {
        addNotEquals(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public AddressSelection clientFirstNameLike(String... value) {
        addLike(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public AddressSelection clientFirstNameContains(String... value) {
        addContains(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public AddressSelection clientFirstNameStartsWith(String... value) {
        addStartsWith(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public AddressSelection clientFirstNameEndsWith(String... value) {
        addEndsWith(ClientColumns.FIRST_NAME, value);
        return this;
    }

    public AddressSelection orderByClientFirstName(boolean desc) {
        orderBy(ClientColumns.FIRST_NAME, desc);
        return this;
    }

    public AddressSelection orderByClientFirstName() {
        orderBy(ClientColumns.FIRST_NAME, false);
        return this;
    }

    public AddressSelection clientLastName(String... value) {
        addEquals(ClientColumns.LAST_NAME, value);
        return this;
    }

    public AddressSelection clientLastNameNot(String... value) {
        addNotEquals(ClientColumns.LAST_NAME, value);
        return this;
    }

    public AddressSelection clientLastNameLike(String... value) {
        addLike(ClientColumns.LAST_NAME, value);
        return this;
    }

    public AddressSelection clientLastNameContains(String... value) {
        addContains(ClientColumns.LAST_NAME, value);
        return this;
    }

    public AddressSelection clientLastNameStartsWith(String... value) {
        addStartsWith(ClientColumns.LAST_NAME, value);
        return this;
    }

    public AddressSelection clientLastNameEndsWith(String... value) {
        addEndsWith(ClientColumns.LAST_NAME, value);
        return this;
    }

    public AddressSelection orderByClientLastName(boolean desc) {
        orderBy(ClientColumns.LAST_NAME, desc);
        return this;
    }

    public AddressSelection orderByClientLastName() {
        orderBy(ClientColumns.LAST_NAME, false);
        return this;
    }

    public AddressSelection clientEmail(String... value) {
        addEquals(ClientColumns.EMAIL, value);
        return this;
    }

    public AddressSelection clientEmailNot(String... value) {
        addNotEquals(ClientColumns.EMAIL, value);
        return this;
    }

    public AddressSelection clientEmailLike(String... value) {
        addLike(ClientColumns.EMAIL, value);
        return this;
    }

    public AddressSelection clientEmailContains(String... value) {
        addContains(ClientColumns.EMAIL, value);
        return this;
    }

    public AddressSelection clientEmailStartsWith(String... value) {
        addStartsWith(ClientColumns.EMAIL, value);
        return this;
    }

    public AddressSelection clientEmailEndsWith(String... value) {
        addEndsWith(ClientColumns.EMAIL, value);
        return this;
    }

    public AddressSelection orderByClientEmail(boolean desc) {
        orderBy(ClientColumns.EMAIL, desc);
        return this;
    }

    public AddressSelection orderByClientEmail() {
        orderBy(ClientColumns.EMAIL, false);
        return this;
    }
}
