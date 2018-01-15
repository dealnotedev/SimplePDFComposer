package biz.dealnote.powercodetestapp.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.dealnote.powercodetestapp.Injections;
import biz.dealnote.powercodetestapp.db.IRecentRepository;
import biz.dealnote.powercodetestapp.model.PdfFile;
import biz.dealnote.powercodetestapp.mvp.view.IMainView;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class MainPresenter extends BasePresenter<IMainView> {

    @Inject
    IRecentRepository recentRepository;

    @Inject
    Scheduler mainThreadScheduler;

    private final List<PdfFile> recent;

    public MainPresenter(@Nullable Bundle savedInstanceState) {
        super(savedInstanceState);
        Injections.component().inject(this);

        disposeOnDestroy(recentRepository.observe()
                .observeOn(mainThreadScheduler)
                .subscribe(this::onRecentAdded));

        this.recent = new ArrayList<>();

        loadRecent();
    }

    private void onRecentAdded(PdfFile file){
        int targetPosition = 0;

        recent.add(targetPosition, file);
        callView(view -> view.notifyRecentAdded(targetPosition));
    }

    private void loadRecent() {
        disposeOnDestroy(recentRepository.getRecent()
                .subscribeOn(Schedulers.io())
                .observeOn(mainThreadScheduler)
                .subscribe(this::onRecentReceived, this::onRecentGetError));
    }

    @Override
    public void onGuiCreated(@NonNull IMainView view) {
        super.onGuiCreated(view);
        view.displayRecent(recent);
    }

    private void onRecentGetError(Throwable t){
        t.printStackTrace();
    }

    private void onRecentReceived(List<PdfFile> files){
        this.recent.clear();
        this.recent.addAll(files);
        callView(IMainView::notifyRecentChanged);
    }

    @Override
    protected String tag() {
        return MainPresenter.class.getSimpleName();
    }

    public void fireAddClick() {
        getView().startCreator();
    }
}