package com.salyert.swarathesh.inventoryappproject11;

import android.net.Uri;

public class ProductContract {
    public static final String CONTENT_AUTHORITY =
            "com.salyert.InvotoryTracker";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String DATABASE_NAME = "productsDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String PATH_PRODUCTS = "products";
    public static class TableProducts {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_QNT = "quantity";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_IMAGE = "image";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplier_mail";
    }
}
