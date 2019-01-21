package com.app.letuscs.models.modelLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelLogin implements Serializable
{

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("data")
    @Expose
    private ArrayList<ModelLoginDatum> data = null;
    private final static long serialVersionUID = 6981140710226886459L;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<ModelLoginDatum> getData() {
        return data;
    }

    public void setData(ArrayList<ModelLoginDatum> data) {
        this.data = data;
    }

}
