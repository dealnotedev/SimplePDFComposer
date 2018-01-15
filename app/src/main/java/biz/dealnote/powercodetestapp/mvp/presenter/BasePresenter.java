package biz.dealnote.powercodetestapp.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import biz.dealnote.mvp.core.AbsPresenter;
import biz.dealnote.mvp.core.IMvpView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public abstract class BasePresenter<V extends IMvpView> extends AbsPresenter<V> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    BasePresenter(@Nullable Bundle savedInstanceState) {
        super(savedInstanceState);
    }

    @Override
    public void onDestroyed() {
        compositeDisposable.dispose();
        super.onDestroyed();
    }

    void disposeOnDestroy(Disposable disposable){
        compositeDisposable.add(disposable);
    }
}