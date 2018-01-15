package biz.dealnote.powercodetestapp.pdf;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public interface IPdfCreator {
    @CheckResult
    Completable create(@NonNull File file, @NonNull List<Page> pages);
}