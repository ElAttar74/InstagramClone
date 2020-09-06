package com.organization.instagramclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ProfileTab extends Fragment {
    private EditText edtNmae,edtBio,edtProfession,edtHobbies,edtSport;
    private Button btnUpdate;
    public ProfileTab() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tab,container,false);
        edtNmae = view.findViewById(R.id.edtName);
        edtBio = view.findViewById(R.id.edtBio);
        edtProfession = view.findViewById(R.id.edtProfession);
        edtHobbies = view.findViewById(R.id.edtHobbies);
        edtSport = view.findViewById(R.id.edtSport);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        final ParseUser parseUser = ParseUser.getCurrentUser();

        if (parseUser.get("profileName")== null){
            edtNmae.setText("");
        }else {
            edtNmae.setText(parseUser.get("profileName") +"");
        }
        if (parseUser.get("profileBio") == null){
            edtBio.setText("");
        }else {
            edtBio.setText(parseUser.get("profileBio") + "");
        }

        if (parseUser.get("profileProfession") == null){
            edtProfession.setText("");
        }else {
            edtProfession.setText(parseUser.get("profileProfession") + "");
        }
        if (parseUser.get("profileHobbies") == null){
            edtHobbies.setText("");
        }else {
            edtHobbies.setText(parseUser.get("profileHobbies") + "");
        }
        if (parseUser.get("profileFavSport") == null){
            edtSport.setText("");
        }else {
            edtSport.setText(parseUser.get("profileFavSport") + "");
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseUser.put("profileName", edtNmae.getText().toString());
                parseUser.put("profileBio", edtBio.getText().toString());
                parseUser.put("profileProfession", edtProfession.getText().toString());
                parseUser.put("profileHobbies", edtHobbies.getText().toString());
                parseUser.put("profileFavSport", edtSport.getText().toString());

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Udpating info");
                progressDialog.show();

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

            }
        });
        return view;
    }
}