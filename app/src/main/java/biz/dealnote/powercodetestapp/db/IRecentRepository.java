package biz.dealnote.powercodetestapp.db;

import java.io.File;
import java.util.List;

import biz.dealnote.powercodetestapp.model.PdfFile;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public interface IRecentRepository {
    Observable<PdfFile> observe();
    Single<List<PdfFile>> getRecent();
    Completable put(File file, long createDtm);
}