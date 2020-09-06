package com.organization.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


public class SharedPictureTab extends Fragment implements View.OnClickListener {
    private ImageView imgPost;
    private EditText edtPost;
    private Button btnShare;
    Bitmap receivedImageBitmap;
    public SharedPictureTab() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared_picture_tab,container,false);
        imgPost = view.findViewById(R.id.imgPost);
        edtPost = view.findViewById(R.id.edtPost);
        btnShare = view.findViewById(R.id.btnShare);


        imgPost.setOnClickListener(SharedPictureTab.this);
        btnShare.setOnClickListener(SharedPictureTab.this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgPost:
                if (android.os.Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                }else {
                    getChoosenImage();
                }

                break;

            case R.id.btnShare:
                if (receivedImageBitmap != null) {

                    if (edtPost.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Error: You must specify a description."  , Toast.LENGTH_SHORT).show();


                    } else {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", edtPost.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getContext(), "Done!!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Unknown error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });


                    }

                } else {

                    Toast.makeText(getContext(), "Error: You must select an image."  , Toast.LENGTH_SHORT).show();

                }

                break;
        }

    }

    private void getChoosenImage() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {

            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {

                getChoosenImage();

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {

            if (resultCode == Activity.RESULT_OK) {

                //Do something with your captured image.
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);

                    imgPost.setImageBitmap(receivedImageBitmap);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }
    }
}