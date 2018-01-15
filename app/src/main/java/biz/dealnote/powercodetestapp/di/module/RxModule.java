package biz.dealnote.powercodetestapp.di.module;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Ruslan Kolbasa on 01.09.2017.
 * PowercodeTestApp
 */
@Module()
public class RxModule {
    @Provides
    Scheduler provideMainThreadScheduler(){
        return AndroidSchedulers.mainThread();
    }
}