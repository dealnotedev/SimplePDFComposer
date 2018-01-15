package biz.dealnote.powercodetestapp.mvp.presenter;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.dealnote.mvp.reflect.OnGuiCreated;
import biz.dealnote.powercodetestapp.Injections;
import biz.dealnote.powercodetestapp.domain.IPdfInteractor;
import biz.dealnote.powercodetestapp.mvp.view.IPdfCreateView;
import biz.dealnote.powercodetestapp.pdf.CreateIntent;
import biz.dealnote.powercodetestapp.pdf.Page;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class PdfCreatePresenter extends BasePresenter<IPdfCreateView> {

    @Inject
    IPdfInteractor interactor;

    @Inject
    Scheduler mainScheduler;

    private final List<Page> pages;

    public PdfCreatePresenter(@Nullable Bundle savedInstanceState) {
        super(savedInstanceState);
        Injections.component().inject(this);

        this.pages = new ArrayList<>(1);
        this.pages.add(new Page());
    }

    @Override
    public void onGuiCreated(@NonNull IPdfCreateView viewHost) {
        super.onGuiCreated(viewHost);
        viewHost.displayPageNum(getCurrentPageNum());
    }

    @Override
    protected String tag() {
        return PdfCreatePresenter.class.getSimpleName();
    }

    private int getCurrentPageNum() {
        return pages.size() - 1;
    }

    private boolean canAddPage() {
        return getCurrentPage().getImage() != null;
    }

    private Page getCurrentPage() {
        return pages.get(getCurrentPageNum());
    }

    public void firePhotoFromGallerySelected(Uri uri) {
        getCurrentPage().setImage(uri);
        getView().displayImage(uri);

        resolveButtonAddVisiblity();
        resolveButtonCreateVisibility();
    }

    @OnGuiCreated
    private void resolveButtonAddVisiblity() {
        callView(view -> view.setButtonAddPageVisible(canAddPage()));
    }

    @OnGuiCreated
    private void resolveButtonCreateVisibility() {
        callView(view -> view.setButtonCreateVisible(canCreate()));
    }

    @OnGuiCreated
    private void resolveCommentView() {
        callView(view -> view.diplayComment(getCurrentPage().getComment()));
    }

    @OnGuiCreated
    private void resolveImageView() {
        callView(view -> view.displayImage(getCurrentPage().getImage()));
    }

    private boolean canCreate() {
        return getCurrentPage().getImage() != null;
    }

    public void fireAddPageClick() {
        Page page = new Page();
        pages.add(page);

        resolveButtonAddVisiblity();
        resolveButtonCreateVisibility();
        resolveImageView();
        resolveCommentView();

        getView().displayPageNum(getCurrentPageNum());
    }

    public void fireCommentEdit(CharSequence s) {
        getCurrentPage().setComment(s.toString());
    }

    private boolean creationNow;

    private void setCreationNow(boolean creationNow) {
        this.creationNow = creationNow;
        resolveProgressDialogVisibility();
    }

    @OnGuiCreated
    private void resolveProgressDialogVisibility() {
        callView(view -> view.displayCreationProgress(creationNow));
    }

    private void onCreated() {
        setCreationNow(false);
        callView(IPdfCreateView::close);
    }

    private void onCreationError(Throwable t) {
        setCreationNow(false);
        callView(view -> view.showError(t));
    }

    public void fireCreateClick(String fileName) {
        setCreationNow(true);

        CreateIntent intent = new CreateIntent(fileName, new ArrayList<>(pages));

        disposeOnDestroy(interactor.create(intent)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribe(this::onCreated, this::onCreationError));
    }
}