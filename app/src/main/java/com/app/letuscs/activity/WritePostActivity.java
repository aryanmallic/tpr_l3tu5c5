package com.app.letuscs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.letuscs.R;
import com.app.letuscs.adapter.AdapterImagesUpload;
import com.app.letuscs.models.others.ImageData;
import com.app.letuscs.utility.AppController;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.HelperMethods;
import com.app.letuscs.utility.SharedPref;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class WritePostActivity extends BaseActivity {

    private static final String TAG = WritePostActivity.class.getName();
    private static final int GALLERY_PICK = 1111;
    private static final int CAMERA_IMAGE_REQUEST = 2;
    private static final int PIC_CROP = 999;
    private Context mContext;

    private CoordinatorLayout clParent;
    private GalleryPhoto galleryPhoto;
    private CameraPhoto cameraPhoto;
    private EditText etWritePost;
    private ImageView civProfile;
    private TextView tvName, tvPost;
    private ImageButton ibAddImage;
    private RecyclerView rvImageList;
    private ArrayList<ImageData> imageData;
    private AdapterImagesUpload adapterImagesUpload;
    private Uri picUri;

    private AdapterImagesUpload.ImageUploadSetOnItemClickListner imageUploadSetOnItemClickListner;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_write_post;
    }

    @Override
    protected void initializeComponents() {
        clParent = findViewById(R.id.activity_write_post_clParent);
        etWritePost = findViewById(R.id.activity_write_post_etWritePost);
        civProfile = findViewById(R.id.activity_write_post_civProfile);
        tvName = findViewById(R.id.activity_write_post_tvName);
        tvPost = findViewById(R.id.activity_write_post_tvPost);
        ibAddImage = findViewById(R.id.activity_write_post_ibAddImage);
        rvImageList = findViewById(R.id.activity_write_post_rvImageList);
    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = getApplicationContext();
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        galleryPhoto = new GalleryPhoto(mContext);
        cameraPhoto = new CameraPhoto(mContext);

        imageData = new ArrayList<>();
        imageUploadSetOnItemClickListner = new AdapterImagesUpload.ImageUploadSetOnItemClickListner() {
            @Override
            public void getLongClickPosition(int position) {
                showDeleteImageDialog(position);
            }
        };
        adapterImagesUpload = new AdapterImagesUpload(imageData, mContext, width, imageUploadSetOnItemClickListner);
        rvImageList.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvImageList.setAdapter(adapterImagesUpload);
        rvImageList.setNestedScrollingEnabled(false);

        Glide.with(mContext).load(new SharedPref(mContext).getUserImage())
                //.placeholder(R.drawable.ic_user)
                .centerCrop()
                .into(civProfile);
        tvName.setText(new SharedPref(mContext).getUserName());


        etWritePost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etWritePost.setFocusableInTouchMode(true);
                return false;
            }
        });

        ibAddImage.setOnClickListener(this);
        tvPost.setOnClickListener(this);
    }


    private void showDeleteImageDialog(final int position) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WritePostActivity.this);
        alertDialogBuilder.setMessage("Do you want to remove?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        imageData.remove(position);
                        adapterImagesUpload.updateList(imageData);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.activity_write_post_etWritePost:
                etWritePost.setFocusableInTouchMode(true);
                break;
            case R.id.activity_write_post_ibAddImage:
                if (imageData.size() <= 9) {
                    openMyDialogToChooseImage();
                } else {
                    Toast.makeText(mContext, "Oops! Only 10 images could be uploaded", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.activity_write_post_tvPost:
                if (HelperMethods.isOnline(mContext)) {
                    if (imageData.size() == 0 && etWritePost.getText().toString().trim().equals("")) {
                        Toast.makeText(mContext, "Nothing To Post", Toast.LENGTH_SHORT).show();
                    } else {
                        showReadyToPostDialog();
                    }
                } else {
                    String message = Constants.NO_INTERNET;
                    showSnack(message, clParent);
                }
                break;

            default:
                break;
        }
    }

    private void showReadyToPostDialog() {
        showMyLoader(WritePostActivity.this);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WritePostActivity.this);
        alertDialogBuilder.setMessage("Do you want to post?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        createImageJsonToUpload();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createImageJsonToUpload() {
        JSONObject json = new JSONObject();
        JSONArray post_image_json_array = new JSONArray();
        if (imageData.size() > 0 && imageData != null) {
            try {
                for (int i = 0; i < imageData.size(); i++) {
                    JSONObject filePathJson = new JSONObject();
                    filePathJson.put("file_path", imageData.get(i).getValue());
                    post_image_json_array.put(filePathJson);
                }
                json.put("post_image", post_image_json_array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            try {
                json.put("post_image", post_image_json_array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "JSSONN: " + json.toString());

        MyPostUploadAsyncTask myPostUploadAsyncTask = new MyPostUploadAsyncTask(json.toString(), etWritePost.getText().toString().trim());
        myPostUploadAsyncTask.execute();
    }

    private class MyPostUploadAsyncTask extends AsyncTask {
        String imgStr;
        String postStr;

        public MyPostUploadAsyncTask(String imgStr, String postStr) {
            this.imgStr = imgStr;
            this.postStr = postStr;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            final int[] statusCode = new int[1];
            String url = Constants.BASE_URL + "post";
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (statusCode[0] == 200) {
                        hideMyLoader();
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("success")) {
                                    Log.d(TAG, "RESPONSESSS: " + response.toString());
                                    startActivity(new Intent(mContext, PostActivity.class));
                                    finish();
                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    if (statusCode[0] == 204) {
                        hideMyLoader();
                        showSnack("No Content", clParent);
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "responseCode:" + statusCode[0]);
                    hideMyLoader();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        showSnack("No Connectivity", clParent);
                    } else if (error instanceof AuthFailureError) {
                        showSnack("Authentication Failed", clParent);
                    } else if (error instanceof ServerError) {
                        showSnack("Server Error", clParent);
                    } else if (error instanceof NetworkError) {
                        showSnack("Network Connectivity Error", clParent);
                    } else if (error instanceof ParseError) {
                        showSnack("Try Again", clParent);
                    }
                }
            }) {
                @Override
                protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                    statusCode[0] = response.statusCode;
                    return super.parseNetworkResponse(response);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    NetworkResponse networkResponse = volleyError.networkResponse;
                    if (networkResponse != null) {
                        statusCode[0] = volleyError.networkResponse.statusCode;
                        Log.d(TAG, "networkResponse:" + networkResponse.toString());
                    }
                    return super.parseNetworkError(volleyError);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    String auth = "bearer " + new SharedPref(mContext).getKey();
                    map.put("Authorization", auth);
                    return map;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("post_body", postStr);
                    param.put("post_image", imgStr);
                    return param;
                }
            };
            AppController.getInstance().addToRequestQueue(request);
            return null;
        }
    }

    private void openMyDialogToChooseImage() {
        //Toast.makeText(mContext, "OKaY Dialog", Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.design_image_picker);
        dialog.setCancelable(true);
        dialog.setTitle("Choose image from");

        TextView open_camera = dialog.findViewById(R.id.design_image_picker_camera);
        TextView open_gallery = dialog.findViewById(R.id.design_image_picker_gallery);

        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCameraPermissionGranted()) {
                    dialog.dismiss();
                    try {
                        startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_IMAGE_REQUEST);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    return;
                }
            }
        });
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    dialog.dismiss();
                    startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_PICK);
                } else {
                    return;
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {

            String photoPath = cameraPhoto.getPhotoPath();
            try {
                File camFile = new File(photoPath);
                picUri = Uri.fromFile(camFile);
                //picUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".my.package.name.provider", camFile);
                Intent intent = CropImage.activity(picUri)
                        .setAspectRatio(1, 1)
                        .getIntent(WritePostActivity.this);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

                Log.d(TAG, "photoPath: " + photoPath);
                Log.d(TAG, "LISTSIZE: " + imageData.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            galleryPhoto.setPhotoUri(data.getData());
            String photoPath = galleryPhoto.getPath();
            try {
                File galFile = new File(photoPath);
                picUri = Uri.fromFile(galFile);
                //picUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".my.package.name.provider", galFile);
                //performCrop();
                Intent intent = CropImage.activity(picUri)
                        .setAspectRatio(1, 1)
                        .getIntent(WritePostActivity.this);

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

                Log.d(TAG, "LISTSIZE: " + imageData.size());
                Log.d(TAG, "photoPath: " + photoPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File file = new File(resultUri.getPath());
                Bitmap bitmap;
                try {
                    bitmap = new Compressor(this)
                            .setMaxWidth(190)
                            .setMaxHeight(190)
                            .setQuality(75)
                            .compressToBitmap(file);
                    imageData.add(new ImageData("image", encode(bitmap), bitmap));
                    Log.d(TAG, "Byte SIZE: " + BitmapCompat.getAllocationByteCount(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Bitmap bitmap = decodeUriToBitmap(mContext,resultUri);
                //imageData.add(new ImageData("image", encode(bitmap), bitmap));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterImagesUpload.updateList(imageData);
                    }
                }, 200);
                Log.d(TAG, "LISTSIZZ: " + imageData.size());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private String encode(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bao);

        byte[] ba = bao.toByteArray();
        String encodedImage = Base64.encodeToString(ba, 0);
        return encodedImage;

    }
}