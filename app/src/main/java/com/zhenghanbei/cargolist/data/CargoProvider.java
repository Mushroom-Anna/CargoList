package com.zhenghanbei.cargolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 * @author zhenghanbei
 * @date 2017/12/22
 */

public class CargoProvider extends ContentProvider {
    private static final int CARGO = 100;
    private static final int CARGO_ITEM = 101;
    private CargoDbHelper mDbHelper;
    /**
     *uri matcher
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(CargoContract.CargoEntry.CONTENT_AUTHORITY, CargoContract.CargoEntry.PATH_CARGO, CARGO);
        sUriMatcher.addURI(CargoContract.CargoEntry.CONTENT_AUTHORITY, CargoContract.CargoEntry.PATH_CARGO + "/#", CARGO_ITEM);
    }
    @Override
    public boolean onCreate() {
        mDbHelper = new CargoDbHelper(getContext());
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        /**cursor to hold result*/
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case CARGO:
                cursor = database.query(CargoContract.CargoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CARGO_ITEM:
                //get selection and selection args from uri
                selection = CargoContract.CargoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(CargoContract.CargoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri: " + uri);
        }
        //set notification uri
        cursor.setNotificationUri(getContext().getContentResolver(), CargoContract.CargoEntry.CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        //check if data is valid
        String name = values.getAsString(CargoContract.CargoEntry.COLUMN_CARGO_NAME);
        Integer price = values.getAsInteger(CargoContract.CargoEntry.COLUMN_CARGO_PRICE);
        Integer quantity = values.getAsInteger(CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY);
        Integer sales = values.getAsInteger(CargoContract.CargoEntry.COLUMN_CARGO_SALES);
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Cargo name is invalid!");
        }
        if (price != null && price < 0){
            throw new IllegalArgumentException("Cargo price is invalid!");
        }
        if (quantity != null && quantity < 0){
            throw new IllegalArgumentException("Cargo quantity is invalid!");
        }
        if (sales != null && sales < 0){
            throw new IllegalArgumentException("Cargo sales is invalid");
        }
        //the id of return rows
        long id;
        Uri result;
        switch (sUriMatcher.match(uri)){
            case CARGO:
                id = database.insert(CargoContract.CargoEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Cannot insert data into this uri: " + uri);
        }
        //send notification change
        getContext().getContentResolver().notifyChange(CargoContract.CargoEntry.CONTENT_URI, null);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int id;
        switch (sUriMatcher.match(uri)){
            case CARGO:
                id = database.delete(CargoContract.CargoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CARGO_ITEM:
                //get selection and selection args from uri
                selection = CargoContract.CargoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                id = database.delete(CargoContract.CargoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete data from uri: " + uri);
        }
        //send notification change
        getContext().getContentResolver().notifyChange(CargoContract.CargoEntry.CONTENT_URI, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int id;
        switch (sUriMatcher.match(uri)){
            case CARGO:
                id = updateCargo(uri, values, selection, selectionArgs);
                break;
            case CARGO_ITEM:
                //get selection and selection args from uri
                selection = CargoContract.CargoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                id = updateCargo(uri, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot update data from uri: " + uri);
        }
        //send notification change
        getContext().getContentResolver().notifyChange(CargoContract.CargoEntry.CONTENT_URI, null);
        return id;
    }

    /**
     * check the data when updating a cargo
     * @return update row
     */
    private int updateCargo(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        if (values.containsKey(CargoContract.CargoEntry.COLUMN_CARGO_NAME)){
            String name = values.getAsString(CargoContract.CargoEntry.COLUMN_CARGO_NAME);
            if (name == null || name.isEmpty()){
                throw new IllegalArgumentException("Cargo name is invalid!");
            }
        }
        if (values.containsKey(CargoContract.CargoEntry.COLUMN_CARGO_PRICE)){
            Integer price = values.getAsInteger(CargoContract.CargoEntry.COLUMN_CARGO_PRICE);
            if (price != null && price < 0){
                throw new IllegalArgumentException("Cargo price is invalid!");
            }
        }
        if (values.containsKey(CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY)){
            Integer quantity = values.getAsInteger(CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY);
            if (quantity != null && quantity < 0){
                throw new IllegalArgumentException("Cargo quantity is invalid!");
            }
        }
        if (values.containsKey(CargoContract.CargoEntry.COLUMN_CARGO_SALES)){
            Integer sales = values.getAsInteger(CargoContract.CargoEntry.COLUMN_CARGO_SALES);
            if (sales != null && sales < 0){
                throw new IllegalArgumentException("Cargo sales is invalid");
            }
        }
        return database.update(CargoContract.CargoEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
