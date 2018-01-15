package biz.dealnote.powercodetestapp.mvp.view;

import android.net.Uri;

import biz.dealnote.mvp.core.IMvpView;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public interface IPdfCreateView extends IMvpView {
    void displayImage(Uri uri);
    void setButtonAddPageVisible(boolean visible);
    void setButtonCreateVisible(boolean visible);
    void diplayComment(String comment);
    void displayPageNum(int num);

    void showError(Throwable t);

    void displayCreationProgress(boolean creationNow);

    void close();
}