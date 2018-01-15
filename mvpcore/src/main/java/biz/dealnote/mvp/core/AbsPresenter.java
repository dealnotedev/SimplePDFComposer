package biz.dealnote.mvp.core;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import biz.dealnote.mvp.Logger;
import biz.dealnote.mvp.reflect.AnnotatedHandlerFinder;
import biz.dealnote.mvp.reflect.EventHandler;

/**
 * Created by admin on 11.07.2016.
 * mvpcore
 */
public abstract class AbsPresenter<V extends IMvpView> implements IPresenter<V> {

    private WeakReference<V> mViewHost;

    private boolean mIsGuiReady;
    private boolean mDestroyed;
    private boolean mResumed;

    public AbsPresenter(@Nullable Bundle savedInstanceState){

    }

    @Override
    public void onViewHostAttached(@NonNull V viewHost) {
        this.mViewHost = new WeakReference<>(viewHost);
    }

    @Override
    public void onViewHostDetached() {
        this.mViewHost = null;
    }

    @CallSuper
    @Override
    public void onGuiCreated(@NonNull V viewHost) {
        mIsGuiReady = true;
        executeAllResolveViewMethods();
    }

    private void executeAllResolveViewMethods(){
        Set<EventHandler> resolveMethodHandlers =
                AnnotatedHandlerFinder.findAllOnGuiCreatedHandlers(this, AbsPresenter.class);

        for(EventHandler handler : resolveMethodHandlers){
            if(!handler.isValid()) continue;

            try {
                handler.handle();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGuiDestroyed() {
        mIsGuiReady = false;
    }

    @Override
    public boolean isGuiResumed() {
        return mResumed;
    }

    @Override
    public void onGuiResumed() {
        mResumed = true;
    }

    @Override
    public void onGuiPaused() {
        mResumed = false;
    }

    @Override
    public void onDestroyed() {
        mDestroyed = true;
    }

    public boolean isDestroyed() {
        return mDestroyed;
    }

    @CallSuper
    public void saveState(@NonNull Bundle outState) {

    }

    @Override
    public boolean isGuiReady() {
        return mIsGuiReady;
    }

    public V getView() {
        return mViewHost == null ? null : mViewHost.get();
    }

    @Override
    public boolean isViewHostAttached() {
        return getView() != null;
    }

    protected void callView(ViewAction<V> action){
        if(isGuiReady()){
            action.call(getView());
        }
    }

    protected abstract String tag();
}