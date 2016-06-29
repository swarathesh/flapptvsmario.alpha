package com.salyert.swarathesh.inventoryappproject11;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity {

    private ProductOpenHelper mProductOpenHelper;
    EditText productNameView;
    EditText productQntView;
    EditText productPriceView;
    EditText supplierNameView;
    EditText supplierMailView;
    ImageView productImageView;
    Bitmap image = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        mProductOpenHelper = ProductOpenHelper.getInstance(this);
        productNameView = (EditText) findViewById(R.id.add_product_name);
        productQntView = (EditText) findViewById(R.id.add_product_qnt);
        productPriceView = (EditText) findViewById(R.id.add_product_price);
        supplierNameView = (EditText) findViewById(R.id.add_supplier_name);
        supplierMailView = (EditText) findViewById(R.id.add_suppler_mail);
        productImageView = (ImageView) findViewById(R.id.add_product_image);
        if (image == null) {
            productImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            productImageView.setImageBitmap(image);
        }
    }
    public void addProduct(View view) {
        String productName = productNameView.getText().toString();
        if (productName.trim().equals("")) {
            printError("Product name should be empty");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(productQntView.getText().toString());
            if (quantity < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            printError("Quantity must be a non-negative integer");
            return;
        }
        int price;
        try {
            price = Integer.parseInt(productPriceView.getText().toString());
            if (price < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            printError("Price must be a positive integer");
            return;
        }
        String supplierName = supplierNameView.getText().toString();
        if (supplierName.trim().equals("")) {
            printError("Supplier name should be empty");
            return;
        }
        String supplierMail = supplierMailView.getText().toString();
        if (supplierMail.trim().equals("")) {
            printError("Supplier name should be empty");
            return;
        }
        if (image == null) {
            printError("You have to add image");
            return;
        }
        Product product = new Product(productName, quantity,
                price, supplierName, supplierMail, image);
        try {
            mProductOpenHelper.addProduct(product);
        } catch (SQLException e) {
            printError("Error adding product. Maybe the name already exists in db");
            return;
        }
        super.finish();
    }
    private void printError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private static final int RESULT_LOAD_IMAGE = 64667;
    public void addImage(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            try {
                cursor.moveToFirst();
            } catch (Exception e) {
            }
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            image = BitmapFactory.decodeFile(picturePath);
            productImageView.setImageBitmap(image);
        }
    }
}
