package com.example.xpandit.pushnotifications;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class BaseApplication extends Application {
    private static Context sContext;
    public Thread.UncaughtExceptionHandler defaultUEH;


    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}