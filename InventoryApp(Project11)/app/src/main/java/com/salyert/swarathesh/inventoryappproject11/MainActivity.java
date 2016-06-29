package com.salyert.swarathesh.inventoryappproject11;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Observer {
    public TextView notify;
    ListView listGoods;
    private ProductOpenHelper mProductOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notify = (TextView) findViewById(R.id.notify);
        listGoods = (ListView) findViewById(R.id.list_goods);
        mProductOpenHelper = ProductOpenHelper.getInstance(this);
        mProductOpenHelper.addObserver(this);
        listGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Product product = (Product) listGoods.getItemAtPosition(position);
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent.putExtra("product", product);
                startActivity(detailIntent);
            }
        });
    }
    public void openAddActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
        startActivity(intent);
    }
    public void clearProducts(View view) {
        notify.setText("press add product to enter product");
        clean();
    }
    private void clean() {
        mProductOpenHelper.clearProducts();
    }
    @Override
    protected void onResume() {
        super.onResume();
        update();
        mProductOpenHelper.addObserver(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mProductOpenHelper.removeObserver(this);
    }
    @Override
    public void update() {
        Cursor products = mProductOpenHelper.readProducts();
        ArrayList<Product> results = new ArrayList<>();
        if (products.moveToFirst() == false || products == null) {
            notify.setText("press add product to enter product");
            products.close();
            ProductAdapter adapter = new ProductAdapter(MainActivity.this, results);
            listGoods.setAdapter(adapter);
            listGoods.invalidateViews();
            return;
        }else {
            notify.setText("");
        }
        int productNameIndex = products.getColumnIndex(ProductContract.TableProducts.COLUMN_PRODUCT_NAME);
        int productQntIndex = products.getColumnIndex(ProductContract.TableProducts.COLUMN_PRODUCT_QNT);
        int productPriceIndex = products.getColumnIndex(ProductContract.TableProducts.COLUMN_PRODUCT_PRICE);
        int supplierNameIndex = products.getColumnIndex(ProductContract.TableProducts.COLUMN_SUPPLIER_NAME);
        int supplierMailIndex = products.getColumnIndex(ProductContract.TableProducts.COLUMN_SUPPLIER_EMAIL);
        int productImageIndex = products.getColumnIndex(ProductContract.TableProducts.COLUMN_PRODUCT_IMAGE);
        String productName = products.getString(productNameIndex);
        int productQnt = products.getInt(productQntIndex);
        int productPrice = products.getInt(productPriceIndex);
        String supplierName = products.getString(supplierNameIndex);
        String supplierMail = products.getString(supplierMailIndex);
        byte[] productImage = products.getBlob(productImageIndex);
        Product product = new Product(productName, productQnt, productPrice,
                supplierName, supplierMail, BitmapUtilities.getBitmap(productImage));
        results.add(product);
        while (products.moveToNext()) {
            productName = products.getString(productNameIndex);
            productQnt = products.getInt(productQntIndex);
            productPrice = products.getInt(productPriceIndex);
            supplierName = products.getString(supplierNameIndex);
            supplierMail = products.getString(supplierMailIndex);
            productImage = products.getBlob(productImageIndex);

            product = new Product(productName, productQnt, productPrice,
                    supplierName, supplierMail, BitmapUtilities.getBitmap(productImage));
            results.add(product);
        }
        products.close();
        ProductAdapter adapter = new ProductAdapter(MainActivity.this, results);
        listGoods.setAdapter(adapter);
        listGoods.invalidateViews();
    }
}
