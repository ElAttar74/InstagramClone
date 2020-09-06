package com.organization.instagramclone;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.organization.instagramclone.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {


    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        binding.setLifecycleOwner(this);
        binding.edtPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                if (i == KeyEvent.KEYCODE_ENTER &&
                        event.getAction() == KeyEvent.ACTION_DOWN) {
                    signUp();
                }
                return false;
            }
        });
        if(ParseUser.getCurrentUser() != null){
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }

        signUp();


        binding.txtHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String match = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
                if (binding.edtEmail.getText().toString().matches(match)) {

                } else
                    binding.edtEmail.setError("Invalid email formating");
            }

        });

        binding.edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String match = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
                if(binding.edtPass.getText().toString().matches(match)){

                }
                else
                    binding.edtPass.setError("Password must contain Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");

            }
        });

    }


    public void signUp(){
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtEmail.getText().toString().equals("")||
                        binding.edtUser.getText().toString().equals("")||
                        binding.edtPass.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this, "Email, User or password is required", Toast.LENGTH_SHORT).show();
                }else {
                    ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(binding.edtEmail.getText().toString());
                    parseUser.setUsername(binding.edtUser.getText().toString());
                    parseUser.setPassword(binding.edtPass.getText().toString());
                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setMessage("Signning up " + binding.edtUser.getText().toString());
                    progressDialog.show();
                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Toast.makeText(SignUpActivity.this, "sign up successfully", Toast.LENGTH_LONG).show();
                                transitionToSocialMediaActivity();
                            }else {
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            progressDialog.dismiss();
                        }
                    });

                }

            }
        });
    }

    public void rootLayoutTapped (View view){
        try {
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void transitionToSocialMediaActivity(){
        Intent intent =new Intent(SignUpActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }

}