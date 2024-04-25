package com.skyncalci.Floating;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    public static final String CHANNEL_ID="SKY!N Floating Mode";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
