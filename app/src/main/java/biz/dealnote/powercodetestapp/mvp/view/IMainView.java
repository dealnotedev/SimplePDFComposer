package biz.dealnote.powercodetestapp.mvp.view;

import java.util.List;

import biz.dealnote.mvp.core.IMvpView;
import biz.dealnote.powercodetestapp.model.PdfFile;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public interface IMainView extends IMvpView {
    void displayRecent(List<PdfFile> files);
    void notifyRecentChanged();
    void notifyRecentAdded(int position);

    void startCreator();
}