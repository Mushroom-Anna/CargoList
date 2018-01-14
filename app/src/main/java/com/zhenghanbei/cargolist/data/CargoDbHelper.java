package com.zhenghanbei.cargolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhenghanbei on 2017/12/22.
 */

public class CargoDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cargo.db";
    private static final int DATABASE_VERSION = 1;
    /**constructor*/
    CargoDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        //CREATE TABLE cargo(_id INTEGER NOT NULL AUTOINCREMENT,
        // name TEXT NOT NULL,
        // price INTEGER NOT NULL,
        // quantity INTEGER NOT NULL DEFAULT 0,
        // sales INTEGER NOT NULL DEFAULT 0);
        final String CREATE_TABLE = "CREATE TABLE " + CargoContract.CargoEntry.TABLE_NAME + "(" +
                CargoContract.CargoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CargoContract.CargoEntry.COLUMN_CARGO_NAME + " TEXT NOT NULL," +
                CargoContract.CargoEntry.COLUMN_CARGO_PRICE + " INTEGER NOT NULL DEFAULT 0," +
                CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
                CargoContract.CargoEntry.COLUMN_CARGO_SALES + " INTEGER NOT NULL DEFAULT 0);";
        //execute
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
