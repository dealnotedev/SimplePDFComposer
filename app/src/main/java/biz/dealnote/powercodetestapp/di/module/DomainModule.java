package biz.dealnote.powercodetestapp.di.module;

import android.content.Context;

import biz.dealnote.powercodetestapp.db.IRecentRepository;
import biz.dealnote.powercodetestapp.domain.IPdfInteractor;
import biz.dealnote.powercodetestapp.domain.PdfInteractor;
import biz.dealnote.powercodetestapp.pdf.IPdfCreator;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Ruslan Kolbasa on 01.09.2017.
 * PowercodeTestApp
 */
@Module(includes = {RepositoryModule.class, PdfModule.class, ContextModule.class})
public class DomainModule {
    @Provides
    IPdfInteractor providePdfCreator(Context context, IRecentRepository recentRepository, IPdfCreator creator){
        return new PdfInteractor(context, recentRepository, creator);
    }
}