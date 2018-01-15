package biz.dealnote.powercodetestapp;

import android.app.Application;
import biz.dealnote.powercodetestapp.di.DaggerAppComponent;
import biz.dealnote.powercodetestapp.di.module.ContextModule;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Injections.component = DaggerAppComponent
                .builder()
                .contextModule(new ContextModule(this))
                .build();
    }
}