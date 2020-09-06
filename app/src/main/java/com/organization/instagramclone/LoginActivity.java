package com.organization.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.organization.instagramclone.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        binding.edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                if (i == KeyEvent.KEYCODE_ENTER &&
                        event.getAction() == KeyEvent.ACTION_DOWN) {
                    logIn();
                }
                return false;
            }
        });
        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();
            //transitionToSocialMediaActivity();
        }
        logIn();

        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });


    }

    public void logIn(){
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(binding.edtUser.getText().toString(),binding.edtPassword.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null && e == null){
                                    Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                    transitionToSocialMediaActivity();
                                }else {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    public void loginLayoutTabbed (View view){
        try {
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void transitionToSocialMediaActivity(){
        Intent intent =new Intent(LoginActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}