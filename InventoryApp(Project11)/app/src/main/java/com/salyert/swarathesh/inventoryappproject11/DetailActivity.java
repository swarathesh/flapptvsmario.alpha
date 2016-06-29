package com.salyert.swarathesh.inventoryappproject11;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements Observer {

    private Product mProduct;
    private ProductOpenHelper mProductOpenHelper;
    TextView productNameView;
    TextView productQntView;
    TextView productPriceView;
    TextView supplierNameView;
    TextView supplierMailView;
    ImageView productImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        mProduct = getIntent().getExtras().getParcelable("product");
        mProductOpenHelper = ProductOpenHelper.getInstance(this);
        productNameView = (TextView) findViewById(R.id.detail_product_name);
        productQntView = (TextView) findViewById(R.id.detail_product_qnt);
        productPriceView = (TextView) findViewById(R.id.detail_product_price);
        supplierNameView = (TextView) findViewById(R.id.detail_supplier_name);
        supplierMailView = (TextView) findViewById(R.id.detail_suppler_mail);
        productImageView = (ImageView) findViewById(R.id.detail_product_image);
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
        mProduct = mProductOpenHelper.getProductDetail(mProduct.getProductName());
        productNameView.setText(mProduct.getProductName());
        productQntView.setText(Integer.toString(mProduct.getQuantity()));
        productPriceView.setText(Integer.toString(mProduct.getPrice()));
        supplierNameView.setText(mProduct.getSupplierName());
        supplierMailView.setText(mProduct.getSupplierMail());
        productImageView.setImageBitmap(mProduct.getImageAsBitmap());
    }
    public void increaseQuantity(View view) {
        mProduct.setQuantity(mProduct.getQuantity() + 1);
        mProductOpenHelper.updateProduct(mProduct);
    }
    public void decreaseQuantity(View view) {
        int quantity = mProduct.getQuantity();
        if (quantity < 1) {
            Toast.makeText(this, "Quantity can't be negative", Toast.LENGTH_LONG).show();
            return;
        }
        mProduct.setQuantity(mProduct.getQuantity() - 1);
        mProductOpenHelper.updateProduct(mProduct);
    }
    public void orderMore(View view){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mProduct.getSupplierMail()});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "InventoryApp order for " + mProduct.getProductName());
        StringBuilder mailText = new StringBuilder();
        mailText.append(String.format(Locale.getDefault(),
                "%s,%s", mProduct.getSupplierName(), System.getProperty("line.separator")));
        mailText.append(String.format(Locale.getDefault(),
                "We would like to order a shipment of %s from you%s",
                mProduct.getProductName(), System.getProperty("line.separator")));
        emailIntent.putExtra(Intent.EXTRA_TEXT, mailText.toString());
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }
    public void deleteProduct(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mProductOpenHelper.removeObserver(DetailActivity.this);
                mProductOpenHelper.deleteProduct(mProduct.getProductName());

                DetailActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                return;
            }
        });
        builder.setTitle("Confirm deletion");
        builder.setMessage("Are you sure you want to delete this product?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
