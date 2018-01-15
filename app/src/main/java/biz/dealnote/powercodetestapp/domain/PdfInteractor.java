package biz.dealnote.powercodetestapp.domain;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import biz.dealnote.powercodetestapp.db.IRecentRepository;
import biz.dealnote.powercodetestapp.pdf.CreateIntent;
import biz.dealnote.powercodetestapp.pdf.IPdfCreator;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class PdfInteractor implements IPdfInteractor {

    //private final Context context;
    private final IRecentRepository recentRepository;
    private final IPdfCreator creator;

    public PdfInteractor(Context context, IRecentRepository recentRepository, IPdfCreator creator) {
        this.recentRepository = recentRepository;
        this.creator = creator;
        //this.context = context;
    }

    @Override
    public Completable create(final CreateIntent intent) {
        return Single.fromCallable(() -> getTargetFile(intent.getFileName()))
                .flatMapCompletable(file -> creator
                        .create(file, intent.getPages())
                        .andThen(recentRepository.put(file, System.currentTimeMillis())))
                .delay(2, TimeUnit.SECONDS);
    }

    private File getTargetFile(String origFileName) throws IOException {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        //File directory = new File(documentsDir, "Powercode");

        if (!directory.isDirectory() && !directory.mkdirs()) {
            throw new IOException("Unable to create directory");
        }

        File targetFile = null;

        int count = 0;

        boolean noExist = false;

        while (!noExist) {
            String suffix = count == 0 ? "" : "(" + String.valueOf(count) + ")";
            String fileName = origFileName + suffix + ".pdf";

            File file = new File(directory, fileName);

            if (!file.exists()) {
                targetFile = file;
                noExist = true;
            } else {
                count++;
            }
        }

        return targetFile;
    }
}