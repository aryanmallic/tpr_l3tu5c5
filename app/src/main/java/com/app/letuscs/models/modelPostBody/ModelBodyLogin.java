package com.app.letuscs.models.modelPostBody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelBodyLogin implements Serializable {

    @SerializedName("type")
    @Expose
    String type;
}
