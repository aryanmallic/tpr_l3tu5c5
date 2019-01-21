package com.app.letuscs.models.others;

import android.graphics.Bitmap;

public class ImageData {
    String upload_type;
    String value;

    Bitmap imageBitmap;


    public ImageData(String upload_type, String value, Bitmap imageBitmap) {
        this.upload_type = upload_type;
        this.value = value;
        this.imageBitmap = imageBitmap;
    }


    public String getUpload_type() {
        return upload_type;
    }

    public void setUpload_type(String upload_type) {
        this.upload_type = upload_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
