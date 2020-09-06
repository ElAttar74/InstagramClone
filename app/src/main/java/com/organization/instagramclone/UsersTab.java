package com.organization.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView lstUsers;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private TextView txtLoadingData;
    public UsersTab() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_users_tab, container, false);
        lstUsers = view.findViewById(R.id.lstUsers);
        txtLoadingData = view.findViewById(R.id.txtLoadingData);

        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, arrayList);
        lstUsers.setOnItemClickListener(UsersTab.this);
        lstUsers.setOnItemLongClickListener(UsersTab.this);
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    if (objects.size() >0 ){
                        for (ParseUser users : objects){
                            arrayList.add(users.getUsername());
                        }
                        lstUsers.setAdapter(arrayAdapter);
                        txtLoadingData.animate().alpha(0).setDuration(2000);
                        lstUsers.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(getContext(), UsersPostsActivity.class);
        intent.putExtra("username",arrayList.get(i));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(i));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (object != null && e == null){

                }

            }
        });
        return true;
    }
}