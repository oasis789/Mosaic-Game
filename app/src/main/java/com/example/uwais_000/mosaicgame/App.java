package com.example.uwais_000.mosaicgame;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by uwais_000 on 27/07/2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(GameMetaData.class);
        ParseObject.registerSubclass(GameStateData.class);
        Parse.initialize(this, "YIEkvCK1GjxXqWM4j1HPpbdk2w7zSJEqN0iuWGWj", "fAl9T3RZLVLVhnqtrZkLVdqpr104wz6P9kGMaCjT");

    }
}
