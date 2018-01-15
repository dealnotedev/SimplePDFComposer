package biz.dealnote.powercodetestapp.domain;

import biz.dealnote.powercodetestapp.pdf.CreateIntent;
import io.reactivex.Completable;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public interface IPdfInteractor {
    Completable create(CreateIntent intent);
}