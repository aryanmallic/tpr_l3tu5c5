package com.app.letuscs.models.modelPostCount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelPostCount implements Serializable
{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("no_of_post")
    @Expose
    private int noOfPost;
    private final static long serialVersionUID = 8717742110839781330L;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNoOfPost() {
        return noOfPost;
    }

    public void setNoOfPost(Integer noOfPost) {
        this.noOfPost = noOfPost;
    }


}
