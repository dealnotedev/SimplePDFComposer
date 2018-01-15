package biz.dealnote.powercodetestapp.di.module;

import android.content.Context;

import javax.inject.Singleton;

import biz.dealnote.powercodetestapp.db.IRecentRepository;
import biz.dealnote.powercodetestapp.db.RecentRepository;
import biz.dealnote.powercodetestapp.pdf.AndroidPdfCreator;
import biz.dealnote.powercodetestapp.pdf.IPdfCreator;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Ruslan Kolbasa on 01.09.2017.
 * PowercodeTestApp
 */
@Module(includes = {ContextModule.class})
public class PdfModule {
    @Provides
    IPdfCreator providePdfCreator(Context context){
        return new AndroidPdfCreator(context);
    }
}