package biz.dealnote.powercodetestapp.di;

import javax.inject.Singleton;

import biz.dealnote.powercodetestapp.di.module.DomainModule;
import biz.dealnote.powercodetestapp.di.module.PdfModule;
import biz.dealnote.powercodetestapp.di.module.RepositoryModule;
import biz.dealnote.powercodetestapp.di.module.RxModule;
import biz.dealnote.powercodetestapp.mvp.presenter.MainPresenter;
import biz.dealnote.powercodetestapp.mvp.presenter.PdfCreatePresenter;
import dagger.Component;

/**
 * Created by Ruslan Kolbasa on 04.07.2017.
 * PowercodeTestApp
 */
@Singleton
@Component (modules = {RepositoryModule.class, RxModule.class, DomainModule.class})
public interface AppComponent {
    void inject(MainPresenter presenter);
    void inject(PdfCreatePresenter pdfCreatePresenter);
}