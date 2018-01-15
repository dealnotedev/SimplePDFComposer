package biz.dealnote.powercodetestapp.utils;

import io.reactivex.CompletableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 21.02.2017.
 * mosst-moneytransfer-android
 */
public class RxUtils {

    public static CompletableTransformer applyCompletableIOToMainSchedulers() {
        return completable -> completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> applySingleIOToMainSchedulers() {
        return single -> single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}