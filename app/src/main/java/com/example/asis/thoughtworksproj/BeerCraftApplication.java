package com.example.asis.thoughtworksproj;

import android.app.Application;

/**
 * Created by ashishkumarpolai on 4/10/2017.
 * WILL BE CALLED BEFORE THE MAINACTIVITY
 */

public class BeerCraftApplication extends Application {

    public static BeerCraftApplication appInstance;

    public static BeerCraftApplication getInstance()
    {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
     }
}
