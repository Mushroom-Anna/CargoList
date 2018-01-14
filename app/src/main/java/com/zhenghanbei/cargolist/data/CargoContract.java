package com.zhenghanbei.cargolist.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 * @author zhenghanbei
 * @date 2017/12/22
 */

public class CargoContract {
    /**constructor*/
    CargoContract(){}

    public static final class CargoEntry implements BaseColumns{
        public static final String TABLE_NAME = "cargo";
        public static final String _ID = "_id";
        public static final String COLUMN_CARGO_NAME = "name";
        public static final String COLUMN_CARGO_PRICE = "price";
        public static final String COLUMN_CARGO_QUANTITY = "quantity";
        public static final String COLUMN_CARGO_SALES = "sales";

        public static final String CONTENT_AUTHORITY = "com.zhenghanbei.cargolist";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_CARGO = "cargo";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CARGO);


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARGO;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARGO;

    }

}
