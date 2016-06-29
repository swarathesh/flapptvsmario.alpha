package com.salyert.swarathesh.inventoryappproject11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class ProductOpenHelper extends SQLiteOpenHelper implements Observable {
    private ArrayList<Observer> mObservers = new ArrayList<>();
    private static ProductOpenHelper sInstance;
    public static ProductOpenHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ProductOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private ProductOpenHelper(Context context) {
        super(context, ProductContract.DATABASE_NAME, null, ProductContract.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HABITS_TABLE = "CREATE TABLE " + ProductContract.TableProducts.TABLE_NAME +
                "(" +
                ProductContract.TableProducts.COLUMN_PRODUCT_NAME + " TEXT PRIMARY KEY NOT NULL, " +
                ProductContract.TableProducts.COLUMN_PRODUCT_QNT + " INTEGER NOT NULL, " +
                ProductContract.TableProducts.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, " +
                ProductContract.TableProducts.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
                ProductContract.TableProducts.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL, " +
                ProductContract.TableProducts.COLUMN_PRODUCT_IMAGE + " BLOB NOT NULL" +
                ")";
        db.execSQL(CREATE_HABITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ProductContract.TableProducts.TABLE_NAME);
            onCreate(db);
        }
    }
    public void addProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ProductContract.TableProducts.COLUMN_PRODUCT_NAME, product.getProductName());
            values.put(ProductContract.TableProducts.COLUMN_PRODUCT_QNT, product.getQuantity());
            values.put(ProductContract.TableProducts.COLUMN_PRODUCT_PRICE, product.getPrice());
            values.put(ProductContract.TableProducts.COLUMN_SUPPLIER_NAME, product.getSupplierName());
            values.put(ProductContract.TableProducts.COLUMN_SUPPLIER_EMAIL, product.getSupplierMail());
            values.put(ProductContract.TableProducts.COLUMN_PRODUCT_IMAGE, product.getImageAsBytes());
            db.insertOrThrow(ProductContract.TableProducts.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw e;
        } finally {
            db.endTransaction();
        }
        notifyObservers();
    }
    public Cursor readProducts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.query(ProductContract.TableProducts.TABLE_NAME,
                null, null, null, null, null, null);
        return result;
    }
    public void updateProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(ProductContract.TableProducts.COLUMN_PRODUCT_QNT, product.getQuantity());
        int rows = db.update(ProductContract.TableProducts.TABLE_NAME,
                values,
                ProductContract.TableProducts.COLUMN_PRODUCT_NAME + "= ?",
                new String[]{product.getProductName()});
        if (rows == 1) {
            db.setTransactionSuccessful();
            db.endTransaction();
            // notify observers that the database has changed
            notifyObservers();
        } else {
            db.endTransaction();
            throw new SQLException("Trying to update non-existent product");
        }
    }
    public void deleteProduct(String productName) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int affectedRows = 0;
        try {
            affectedRows = db.delete(ProductContract.TableProducts.TABLE_NAME,
                    ProductContract.TableProducts.COLUMN_PRODUCT_NAME + " = ?",
                    new String[]{productName});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String message = String.format(Locale.getDefault(),
                    "Trying to delete %d instead of one", affectedRows);
            Log.d(getClass().getName(), message);
        } finally {
            db.endTransaction();
        }
        notifyObservers();
    }
    public void clearProducts() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ProductContract.TableProducts.TABLE_NAME);
        onCreate(db);
        notifyObservers();
    }
    public Product getProductDetail(String productName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.query(ProductContract.TableProducts.TABLE_NAME,
                null, ProductContract.TableProducts.COLUMN_PRODUCT_NAME + "= ?", new String[]{productName}, null, null, null);
        if (result.moveToFirst() == false) {
            return null;
        }
        int productQntIndex = result.getColumnIndex(ProductContract.TableProducts.COLUMN_PRODUCT_QNT);
        int productPriceIndex = result.getColumnIndex(ProductContract.TableProducts.COLUMN_PRODUCT_PRICE);
        int supplierNameIndex = result.getColumnIndex(ProductContract.TableProducts.COLUMN_SUPPLIER_NAME);
        int supplierMailIndex
                = result.getColumnIndex(ProductContract.TableProducts.COLUMN_SUPPLIER_EMAIL);
        int productImageIndex = result.getColumnIndex(ProductContract.TableProducts.COLUMN_PRODUCT_IMAGE);
        int productQnt = result.getInt(productQntIndex);
        int productPrice = result.getInt(productPriceIndex);
        String supplierName = result.getString(supplierNameIndex);
        String supplierMail = result.getString(supplierMailIndex);
        byte[] productImage = result.getBlob(productImageIndex);
        Product product = new Product(productName, productQnt, productPrice,
                supplierName, supplierMail, BitmapUtilities.getBitmap(productImage));
        return product;
    }
    @Override
    public void addObserver(Observer o) {
        if (o != null) {
            mObservers.add(o);
        }
    }
    @Override
    public void removeObserver(Observer o) {
        if (o != null) {
            mObservers.remove(o);
        }
    }
    public void notifyObservers() {
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).update();
        }
    }
}
