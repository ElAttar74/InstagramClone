package com.organization.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UsersPostsActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);
        linearLayout = findViewById(R.id.linearLayout);
        Intent recievedIntentObject = getIntent();
        String recievedUserName = recievedIntentObject.getStringExtra("username");
        setTitle(recievedUserName + "'s Posts");
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("photo");
        parseQuery.whereEqualTo("username", recievedUserName);
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog progressDialog = new ProgressDialog(UsersPostsActivity.this);
        progressDialog.setMessage("Loading posts");
        progressDialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null){
                    for (ParseObject post : objects){
                        final TextView postDescribtion = new TextView(UsersPostsActivity.this);
                        postDescribtion.setText(post.get("image_des") + "");
                        ParseFile parseFile = (ParseFile) post.get("picture");
                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null){
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImageView = new ImageView(UsersPostsActivity.this);
                                    LinearLayout.LayoutParams imageView_params =
                                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(5, 5, 5, 5);
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams des_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    des_params.setMargins(5, 5, 5, 15);
                                    postDescribtion.setLayoutParams(des_params);
                                    postDescribtion.setGravity(Gravity.CENTER);
                                    postDescribtion.setTextColor(Color.BLACK);
                                    postDescribtion.setTextSize(25f);

                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescribtion);
                                }

                            }
                        });

                    }

                }else {
                    Toast.makeText(UsersPostsActivity.this, "doesn't have any posts!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }
        });
    }
}