package com.sherifmakhlouf.goeuro;


import android.app.Application;
import android.content.Context;

import java.util.Locale;

import dagger.ObjectGraph;

public class GoEuroApp extends Application{
    private ObjectGraph objectGraph;
    public static String locale;
    @Override
    public void onCreate() {
        super.onCreate();
        buildObjectGraphAndInject();
        locale = Locale.getDefault().getLanguage();
    }

    public void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static GoEuroApp get(Context context) {
        return (GoEuroApp) context.getApplicationContext();
    }
}
