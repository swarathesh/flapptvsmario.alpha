package com.salyert.swarathesh.inventoryappproject11;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class BitmapUtilities {
    public static Bitmap getBitmap(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
