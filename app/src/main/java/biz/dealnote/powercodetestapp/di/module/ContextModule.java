package biz.dealnote.powercodetestapp.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ruslan Kolbasa on 04.07.2017.
 * PowercodeTestApp
 */
@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext(){
        return context;
    }
}