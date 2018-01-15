package biz.dealnote.powercodetestapp.di.module;

import android.content.Context;

import javax.inject.Singleton;

import biz.dealnote.powercodetestapp.db.IRecentRepository;
import biz.dealnote.powercodetestapp.db.RecentRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Ruslan Kolbasa on 01.09.2017.
 * PowercodeTestApp
 */
@Module(includes = {ContextModule.class})
public class RepositoryModule {
    @Singleton
    @Provides
    IRecentRepository provideRecentRepository(Context context){
        return new RecentRepository(context);
    }
}