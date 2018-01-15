package biz.dealnote.powercodetestapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biz.dealnote.powercodetestapp.model.PdfFile;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class RecentRepository implements IRecentRepository {

    //private final Context app;
    private final DbHelper helper;
    private final PublishSubject<PdfFile> publisher;

    public RecentRepository(Context context) {
        //this.app = context.getApplicationContext();
        this.helper = DbHelper.getInstance(context.getApplicationContext());
        this.publisher = PublishSubject.create();
    }

    @Override
    public Observable<PdfFile> observe() {
        return publisher;
    }

    @Override
    public Single<List<PdfFile>> getRecent() {
        return getRecentEntities().compose(entitiesToPdf());
    }

    @Override
    public Completable put(File file, long createDtm) {
        return Completable
                .fromAction(() -> {
                    ContentValues cv = new ContentValues();
                    cv.put(RecentColumns.PATH, file.getAbsolutePath());
                    cv.put(RecentColumns.CREATE_DATETIME, createDtm);
                    helper.getReadableDatabase().insert(RecentColumns.TABLENAME, null, cv);
                })
                .doOnComplete(() -> publisher.onNext(new PdfFile(file, new Date(createDtm))));
    }

    private SingleTransformer<List<RecentEntity>, List<PdfFile>> entitiesToPdf() {
        return upstream -> upstream
                .map(entities -> {
                    List<PdfFile> found = new ArrayList<>(entities.size());

                    for (RecentEntity entity : entities) {
                        File file = new File(entity.path);

                        if (file.isFile()) {
                            found.add(new PdfFile(file, new Date(entity.createDtm)));
                        }
                    }

                    return found;
                });
    }

    private Single<List<RecentEntity>> getRecentEntities() {
        return Single.create(emitter -> {
            String[] columns = {RecentColumns.PATH, RecentColumns.CREATE_DATETIME};
            Cursor cursor = helper.getReadableDatabase().query(RecentColumns.TABLENAME, columns,
                    null, null, null, null, RecentColumns._ID + " DESC");

            List<RecentEntity> entities = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                if (emitter.isDisposed()) {
                    break;
                }

                entities.add(map(cursor));
            }

            cursor.close();
            emitter.onSuccess(entities);
        });
    }

    private static RecentEntity map(Cursor cursor) {
        return new RecentEntity(cursor.getString(0), cursor.getLong(1));
    }

    private static final class RecentEntity {

        final String path;
        final long createDtm;

        private RecentEntity(String path, long createDtm) {
            this.path = path;
            this.createDtm = createDtm;
        }
    }
}