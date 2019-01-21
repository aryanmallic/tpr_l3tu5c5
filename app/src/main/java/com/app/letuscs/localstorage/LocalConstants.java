package com.app.letuscs.localstorage;

import android.util.Log;

import com.app.letuscs.models.postsModel.ModelPosts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalConstants {

    public static final String POST = "Post";
    public static final String USER_IMAGE="USERIMAGE";

    public static List<ModelPosts> getPost() {
        List<ModelPosts> list = new ArrayList<>();
        if (SharedPreferenceUtil.contains(LocalConstants.POST) && SharedPreferenceUtil.getString(LocalConstants.POST, null) != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ModelPosts>>() {
            }.getType();
            list = gson.fromJson(SharedPreferenceUtil.getString(LocalConstants.POST, null), type);
            return list;
        }
        return list;
    }


    public static String getUserOfflineImages(){
        String image="";
        if (SharedPreferenceUtil.contains(LocalConstants.USER_IMAGE)
                && SharedPreferenceUtil.getString(LocalConstants.USER_IMAGE, null) != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<String>() {
            }.getType();
            image = gson.fromJson(SharedPreferenceUtil.getString(LocalConstants.USER_IMAGE, ""), type);
            return image;
        }
        return image;
    }
}
