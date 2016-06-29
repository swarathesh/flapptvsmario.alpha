package com.salyert.swarathesh.inventoryappproject11;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String mProductName;
    private int mQuantity;
    private int mPrice;
    private String mSupplierName;
    private String mSupplierMail;
    private Bitmap mImage;

    public Product(String productName, int productQuantity,
                   int productPrice,
                   String supplierName, String supplierMail,
                   Bitmap image){
        mProductName = productName;
        mQuantity = productQuantity;
        mPrice = productPrice;
        mSupplierName = supplierName;
        mSupplierMail = supplierMail;
        int imageBigSide = Math.max(image.getHeight(), image.getWidth());
        if (imageBigSide > 300){
            int scaleModifier = imageBigSide / 300;
            image = Bitmap.createScaledBitmap(image, image.getWidth()/scaleModifier,
                    image.getHeight()/scaleModifier, true);
        }
        mImage = image;
    }
    protected Product(Parcel in) {
        mProductName = in.readString();
        mQuantity = in.readInt();
        mPrice = in.readInt();
        mSupplierName = in.readString();
        mSupplierMail = in.readString();
        mImage = BitmapUtilities.getBitmap(in.createByteArray());
    }
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    public String getProductName() {
        return mProductName;
    }
    public int getQuantity() {
        return mQuantity;
    }
    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }
    public String getSupplierName() {
        return mSupplierName;
    }
    public String getSupplierMail() {
        return mSupplierMail;
    }
    public int getPrice(){
        return mPrice;
    }
    public Bitmap getImageAsBitmap(){
        return mImage;
    }
    public byte[] getImageAsBytes(){
        return BitmapUtilities.getBytes(mImage);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mProductName);
        parcel.writeInt(mQuantity);
        parcel.writeInt(mPrice);
        parcel.writeString(mSupplierName);
        parcel.writeString(mSupplierMail);
        parcel.writeByteArray(BitmapUtilities.getBytes(mImage));
    }
}
