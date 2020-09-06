package com.organization.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("xR7YMU0wmZrxCodkbOL4wtycGB1hfO5lzwWvbi7y")
                // if defined
                .clientKey("Zit4A6Ud979qNH72w9HAigSNKv2sUtjDQ4PxkI06")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
